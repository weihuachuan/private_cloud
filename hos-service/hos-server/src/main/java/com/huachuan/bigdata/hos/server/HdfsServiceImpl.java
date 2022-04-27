package com.huachuan.bigdata.hos.server;

import com.huachuan.bigdata.hos.core.HosConfiguration;
import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class HdfsServiceImpl implements IHdfsService{
    private static Logger logger = Logger.getLogger(HdfsServiceImpl.class);
    private FileSystem fileSystem;
    private long defaultBlockSize = 128 * 1024 * 1024;
    private long initBlockSize = defaultBlockSize / 2;
    public HdfsServiceImpl() throws Exception {
        // 1 获取文件系统
        Configuration configuration = new Configuration();
        // 配置在集群上运行
        fileSystem = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration);
    }

    @Override
    public void saveFile(String dir, String name,
                         InputStream input, long length, short replication) throws IOException {
        Path dirPath = new Path(dir);
        try {
            if (!fileSystem.exists(dirPath)) {
                boolean succ = fileSystem.mkdirs(dirPath, FsPermission.getDirDefault());
                logger.info("create dir " + dirPath + " success" + succ);
                if (!succ) {
                    throw new IOException("dir create failed:" + dir);
                }
            }
        } catch (FileExistsException ex) {
            //do nothing
        }
        Path path = new Path(dir + "/" + name);
        long blockSize = length <= initBlockSize ? initBlockSize : defaultBlockSize;
        FSDataOutputStream outputStream = fileSystem.create(path, true, 512 * 1024, replication, blockSize);
        try {
            fileSystem.setPermission(path, FsPermission.getFileDefault());
            byte[] buffer = new byte[512 * 1024];
            int len = -1;
            while ((len = input.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } finally {
            input.close();
            outputStream.close();
        }
    }

    @Override
    public void deleteFile(String dir, String name) throws IOException {
        fileSystem.delete(new Path(dir + "/" + name), false);
    }

    @Override
    public InputStream openFile(String dir, String name) throws IOException {
        return fileSystem.open(new Path(dir + "/" + name));
    }

    @Override
    public void mikDir(String dir) throws IOException {
        fileSystem.mkdirs(new Path(dir));
    }

    @Override
    public void deleteDir(String dir) throws IOException {
        this.fileSystem.delete(new Path(dir), true);
    }
}
