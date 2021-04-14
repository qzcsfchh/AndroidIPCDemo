package me.android.ipc.server.ipc;

public enum IPC {
    aidl,
    contentProvider,
    broadcast,
    /**
     * 基于mmap，内存映射文件，效率最高
     * @see <a href='https://github.com/Tencent/MMKV/wiki/android_ipc>link</a>
     */
    mmkv,
    socket,
    pipe,
    mq
}
