// 代码生成时间: 2025-10-14 16:52:00
import com.dropwizard.Application;
import com.dropwizard.setup.Bootstrap;
import com.dropwizard.setup.Environment;
import com.dropwizard.views.View;
import com.dropwizard.views.mustache.MustacheViewRenderer;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.views.ViewBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

// 定义DiskSpaceManager类，继承自Application
public class DiskSpaceManager extends Application<DiskSpaceManagerConfiguration> {

    // 日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(DiskSpaceManager.class);

    // 定义DiskSpaceResource类，实现磁盘空间管理功能
    @Path("/disk-space")
    public static class DiskSpaceResource {

        // 获取磁盘空间信息
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public List<DiskSpaceInfo> getDiskSpaceInfo() {
            try {
                // 获取所有文件系统
                List<FileStore> fileStores = FileSystems.getDefault().getFileStores();

                // 遍历文件系统，获取磁盘空间信息
                List<DiskSpaceInfo> diskSpaceInfos = fileStores.stream()
                        .map(DiskSpaceManager::getFileStoreInfo)
                        .collect(Collectors.toList());

                return diskSpaceInfos;
            } catch (IOException e) {
                LOGGER.error("Error retrieving disk space info", e);
                throw new RuntimeException("Error retrieving disk space info");
            }
        }

        // 从FileStore对象中获取磁盘空间信息
        private static DiskSpaceInfo getFileStoreInfo(FileStore fileStore) throws IOException {
            FileStoreAttributes attributes = new FileStoreAttributes(fileStore);
            return new DiskSpaceInfo(
                    fileStore.name(),
                    attributes.getTotalSpace(),
                    attributes.getUsableSpace(),
                    attributes.getUnallocatedSpace(),
                    attributes.getTotalSpace() - attributes.getUsableSpace()
            );
        }
    }

    // 定义DiskSpaceInfo类，表示磁盘空间信息
    public static class DiskSpaceInfo {
        private final String name;
        private final long totalSpace;
        private final long usableSpace;
        private final long unallocatedSpace;
        private final long usedSpace;

        public DiskSpaceInfo(String name, long totalSpace, long usableSpace, long unallocatedSpace, long usedSpace) {
            this.name = name;
            this.totalSpace = totalSpace;
            this.usableSpace = usableSpace;
            this.unallocatedSpace = unallocatedSpace;
            this.usedSpace = usedSpace;
        }

        public String getName() {
            return name;
        }

        public long getTotalSpace() {
            return totalSpace;
        }

        public long getUsableSpace() {
            return usableSpace;
        }

        public long getUnallocatedSpace() {
            return unallocatedSpace;
        }

        public long getUsedSpace() {
            return usedSpace;
        }
    }

    // 定义FileStoreAttributes类，表示文件存储属性
    private static class FileStoreAttributes {
        private final FileStore fileStore;

        public FileStoreAttributes(FileStore fileStore) {
            this.fileStore = fileStore;
        }

        public long getTotalSpace() throws IOException {
            return fileStore.getTotalSpace();
        }

        public long getUsableSpace() throws IOException {
            return fileStore.getUsableSpace();
        }

        public long getUnallocatedSpace() throws IOException {
            return fileStore.getUnallocatedSpace();
        }
    }

    // 运行Dropwizard应用程序
    public static void main(String[] args) throws Exception {
        new DiskSpaceManager().run(args);
    }

    // 初始化Dropwizard应用程序
    @Override
    public void initialize(Bootstrap<DiskSpaceManagerConfiguration> bootstrap) {
        // 初始化视图渲染器
        bootstrap.addViewRenderer(new MustacheViewRenderer());
        // 添加AssetsBundle资源
        bootstrap.addBundle(new AssetsBundle("/", "/"));
        // 添加视图Bundle资源
        bootstrap.addBundle(new ViewBundle<>());
    }

    // 运行Dropwizard应用程序
    @Override
    public void run(DiskSpaceManagerConfiguration configuration, Environment environment) {
        // 注册DiskSpaceResource资源
        environment.jersey().register(new DiskSpaceResource());
    }
}
