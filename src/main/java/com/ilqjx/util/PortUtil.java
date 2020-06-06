package com.ilqjx.util;

import javax.swing.*;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 判断某个端口是否启动
 */
public class PortUtil {

    public static boolean testPort(int port) {
        try {
            // 等待客户端的请求，与port端口绑定
            ServerSocket socket = new ServerSocket(port);
            // 使服务器释放占有的端口，并且断开与所有客户的连接
            // 及时释放端口，因为这里就是用来判断某个端口是否启动
            socket.close();
            return false;
        } catch (java.net.BindException e) { // 若没有以超级用户身份运行服务器程序，操作系统不允许服务器绑定到1-1023的端口时，会抛出BindException
            return true;
        } catch (IOException e) {
            return true;
        }
    }

    public static void checkPort(int port, String server, boolean shutdown) {
        if (!testPort(port)) {
            if (shutdown) {
                String message = String.format("在端口 %d 未检查得到 %s 启动%n", port, server);
                // 消息提示框
                JOptionPane.showMessageDialog(null, message);
                // 用来结束当前正在运行中的java虚拟机，如果status是非零参数，表示非正常退出
                System.exit(1);
            } else {
                String message = String.format("在端口 %d 未检查得到 %s 启动%n，是否继续？", port, server);
                if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, message)) {
                    System.exit(1);
                }
            }
        }
    }

}
