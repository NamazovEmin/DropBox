package com.example.server.handler;

import com.example.cloud.*;
import com.example.server.db.DataBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {

    private Path serverPath;
    private Path defaultPath;
    private String login;

    public CloudFileHandler() {
        serverPath = Path.of("server_files").toAbsolutePath();
        defaultPath = Path.of("server_files").toAbsolutePath();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        if (cloudMessage instanceof FileRequest fileRequest) {
            ctx.writeAndFlush(new FileMessage(serverPath.resolve(fileRequest.getName())));
        } else if (cloudMessage instanceof FileMessage fileMessage) {
            Files.write(serverPath.resolve(fileMessage.getName()), fileMessage.getData());
            ctx.writeAndFlush(new ListFiles(serverPath));
        }
        else if (cloudMessage instanceof PathUpRequest) {
           if (serverPath.getParent() != null) {
               if (serverPath.getParent().toString().equals(defaultPath.resolve(login).toString())){
                   System.out.println(serverPath.getParent());
                   System.out.println(defaultPath.resolve(login));
                   serverPath = serverPath.getParent();
                   ctx.writeAndFlush(new ListFiles(serverPath));
               }

           }
        } else if (cloudMessage instanceof PathInRequest pathInRequest) {
               if (Files.isDirectory(serverPath.resolve(pathInRequest.getFileName()))) {
                   serverPath = serverPath.resolve(pathInRequest.getFileName());
                   ctx.writeAndFlush(new ListFiles(serverPath));
               }
        } else if (cloudMessage instanceof PathFindRequest pathFindRequest) {
            if (Files.isDirectory(Path.of(pathFindRequest.getFileName()))) {
                serverPath = Path.of(pathFindRequest.getFileName());
                ctx.writeAndFlush(new ListFiles(serverPath));
            }
        } else if (cloudMessage instanceof Reg reg) {
            if(DataBase.insertNewUserInDataBase(reg.getName(),reg.getSurname(),reg.getTeNumber(),reg.getEmail(),reg.getLogin(),reg.getPassword())) {
                reg.setReg(true);
                ctx.writeAndFlush(reg);
            } else {
                ctx.writeAndFlush(reg);
            }
        } else if (cloudMessage instanceof Auth auth) {
            if (DataBase.selectFromUsersByLogin(auth.getLogin(), auth.getPassword(), auth).isAccess()) {
                ctx.writeAndFlush(auth);
                new File(String.valueOf(serverPath.resolve(auth.getLogin()))).mkdirs();
                System.out.println(serverPath.resolve(auth.getLogin()));
            }else {
                ctx.writeAndFlush(auth);
            }
        } else if (cloudMessage instanceof NetworkDirectory networkDirectory) {
            serverPath = serverPath.resolve(networkDirectory.getLogin());
            System.out.println(serverPath);
            login = networkDirectory.getLogin();
            ctx.writeAndFlush(new ListFiles(serverPath));
        } else if (cloudMessage instanceof NewDir newDir) {
            new File(String.valueOf(serverPath.resolve(newDir.getDirName()))).mkdirs();
            ctx.writeAndFlush(new ListFiles(serverPath));
        }
    }
}
