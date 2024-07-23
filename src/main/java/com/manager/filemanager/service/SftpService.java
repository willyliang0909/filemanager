package com.manager.filemanager.service;

import com.manager.filemanager.config.SftpConfig;
import com.manager.filemanager.model.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SftpService {

    private final SftpRemoteFileTemplate sftpTemplate;

    private final SftpConfig sftpConfig;

    public Stream<SftpClient.DirEntry> listFile(String remoteDirectory, boolean includeHidden) {
        return sftpTemplate.execute(session ->
                Arrays.stream(session.list(remoteDirectory))
                        .filter(dirEntry -> includeHidden || !dirEntry.getFilename().startsWith("."))
        );
    }

    public List<Document> listFiles(String remoteDirectory) {

        var path = sftpConfig.getSftpFolder() + remoteDirectory;

        Comparator<SftpClient.DirEntry> comparator1 = Comparator.comparing(dirEntry -> dirEntry.getAttributes().isDirectory());

        return listFile(path, false)
                //.sorted(comparator1.thenComparing(SftpClient.DirEntry::getFilename))
                .map(dirEntry -> {
                    var attributes = Optional.ofNullable(dirEntry.getAttributes());

                    var fileName = dirEntry.getFilename();
                    var typeCode = attributes.map(SftpClient.Attributes::getType).orElse(3);
                    var type = Document.Type.fromType(typeCode);
                    var fullPath = path + fileName;
                    var pathSegments = Arrays.stream(fullPath.split(File.separator))
                            .filter(s -> !s.isBlank())
                            .toArray(String[]::new);
                    var createdTime = attributes
                            .map(SftpClient.Attributes::getCreateTime)
                            .map(FileTime::toInstant)
                            .orElse(null);
                    var accessTime = attributes
                            .map(SftpClient.Attributes::getAccessTime)
                            .map(FileTime::toInstant)
                            .orElse(null);
                    var modifyTime = attributes
                            .map(SftpClient.Attributes::getModifyTime)
                            .map(FileTime::toInstant)
                            .orElse(null);

                    return Document.builder()
                            .id(DigestUtils.md5Hex(fullPath))
                            .name(fileName)
                            .type(type)
                            .path(fullPath)
                            .pathSegments(pathSegments)
                            .createdTime(createdTime)
                            .accessTime(accessTime)
                            .modifyTime(modifyTime)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
