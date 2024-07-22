package com.manager.filemanager.service;

import com.manager.filemanager.config.SftpConfig;
import com.manager.filemanager.model.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

        String[] pathSegments = Arrays.stream(path.split(String.valueOf(File.separatorChar)))
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);

        return listFile(path, false)
                //.sorted(comparator1.thenComparing(SftpClient.DirEntry::getFilename))
                .map(dirEntry -> {
                    var fileName = dirEntry.getFilename();
                    var newPathSegments = Arrays.copyOf(pathSegments, pathSegments.length + 1);
                    newPathSegments[newPathSegments.length - 1] = fileName;
                    return Document.builder()
                            .name(dirEntry.getFilename())
                            .type(Document.Type.fromType(dirEntry.getAttributes().getType()))
                            .pathSegments(newPathSegments)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
