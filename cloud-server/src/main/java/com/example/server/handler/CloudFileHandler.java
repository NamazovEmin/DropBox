package com.example.server.handler;

import com.example.cloud.*;
import com.example.server.db.DataBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {

    private Path serverPath;
    private  final Path defaultPath;
    private String login;

    public CloudFileHandler() {
        serverPath = Path.of("server_files").toAbsolutePath();
        defaultPath = Path.of("server_files").toAbsolutePath();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        if (cloudMessage instanceof FileRequest fileRequest) {
            if (Files.isDirectory(serverPath.resolve(fileRequest.getName()))) {
                Path testPath = serverPath.resolve(fileRequest.getName());
                sendDirAndFiles(ctx,testPath);
            }else {
                System.out.println(serverPath.resolve(fileRequest.getName()));
                ctx.writeAndFlush(new FileMessage(serverPath.resolve(fileRequest.getName())));
            }
        } else if (cloudMessage instanceof FileMessage fileMessage) {
            Files.write(serverPath.resolve(fileMessage.getName()), fileMessage.getData());
            ctx.writeAndFlush(new ListFiles(serverPath));
        }
        else if (cloudMessage instanceof PathUpRequest) {
           if (serverPath.getParent() != null) {
               if (!serverPath.getParent().toString().equals(defaultPath.toString())){
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
        } else if (cloudMessage instanceof NewDirServer newDir) {
            new File(String.valueOf(serverPath.resolve(newDir.getDirName()))).mkdirs();
            ctx.writeAndFlush(new ListFiles(serverPath));
        } else if (cloudMessage instanceof  ShareFile shareFile) {
            String fileLink = randomLink();
            if (DataBase.insertLink(login ,fileLink, serverPath.resolve(shareFile.getFileName()).toString())) {
                shareFile.setLink(fileLink);
                ctx.writeAndFlush(shareFile);
            }
        } else if (cloudMessage instanceof  FileRequestByLink fileRequestByLink) {
           Path linkedPath =  DataBase.findLink(fileRequestByLink.getLink());
           if (linkedPath != null) {
               if (Files.isDirectory(linkedPath)) {
                    sendDirAndFiles(ctx,linkedPath);
               }else {
                   ctx.writeAndFlush(new FileMessage(linkedPath));
               }
           }
        } else if (cloudMessage instanceof  AllMyLinks allMyLinks) {
            if (DataBase.allLinksByLogin(login, allMyLinks) != null ) {
                ctx.writeAndFlush(allMyLinks);
            }
        } else if (cloudMessage instanceof  DeleteUserLinks deleteUserLinks) {
            if (deleteUserLinks.getAll()) {
                DataBase.deleteAllLink(login);
            } else {
                DataBase.deleteCurrentLink(deleteUserLinks.getLink());
            }
        } else if (cloudMessage instanceof  DirectoryAndFileMessage directoryAndFileMessage) {
            for (int i = 0; i < directoryAndFileMessage.getDirName().size(); i++) {
                new File(String.valueOf(serverPath.resolve(directoryAndFileMessage.getDirName().get(i)))).mkdirs();
            }
            for (int i = 0; i < directoryAndFileMessage.getFileName().size(); i++) {
                    Files.write(serverPath.resolve(directoryAndFileMessage.getFileName().get(i)),
                            directoryAndFileMessage.getDataList().get(i));
            }
            ctx.writeAndFlush(new ListFiles(serverPath));
        }
    }

    //стоит ли оставлять такой метод на сервере или поместить его в конструктор new DirectoryAndFileMessage
    //Я знаю что это можно было реализовать через, какую-то библиотеку, но хотелось сделать самому.
    private void sendDirAndFiles(ChannelHandlerContext ctx, Path testPath) throws IOException {
        List<Path> fileList = new ArrayList<>();
        List<Path> dirList = new ArrayList<>();
        List<Path> buffList = new ArrayList<>();
        List<Path> buffDir = new ArrayList<>();
        buffDir.add(testPath);
        while (true) {
            for (Path path : buffDir) {
                dirList.add(path);
                if (Files.list(path) != null) {
                    buffList.addAll(Files.list(path).toList());
                }
            }
            buffDir.clear();
            for (Path path : buffList) {
                if (Files.isDirectory(path)) {
                    buffDir.add(path);
                } else {
                    fileList.add(path);
                }
            }
            buffList.clear();
            if (buffDir.size() == 0) {
                break;
            }
        }
        testPath = testPath.getParent();
        ctx.writeAndFlush(new DirectoryAndFileMessage(dirList,testPath,fileList));
    }

    private String randomLink() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder generate;
        String generatedLink;
        do {
            generate = new StringBuilder(random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString());
        } while (DataBase.checkLink(generate));
        generatedLink = generate.toString();
        return generatedLink;
    }
}
