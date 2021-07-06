package org.darkghast.fms.service;

import java.io.IOException;

/**
 * 由于涉及事务commit与rollback，需要将删除提取为单独的service
 */
public interface FileDeleteService {
    /**
     * 将数据库中的文件信息删除
     *
     * @param Id 目标文件的ID
     * @return 操作成功返回1，否则返回0
     */
    int delete(Long Id) throws IOException;

    /**
     * 在执行删除时需要调用commit才能真正从数据库删除数据
     */
    void commit();

    /**
     * 在执行删除时调用rollback回滚
     */
    void rollback();
}
