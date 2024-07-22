package com.manager.filemanager.service;

import com.manager.filemanager.config.SftpConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.stereotype.Service;

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
        var path = sftpConfig.getSftpFolder() + remoteDirectory;
        return sftpTemplate.execute(session ->
                Arrays.stream(session.list(path))
                        .filter(dirEntry -> includeHidden || !dirEntry.getFilename().startsWith("."))
        );
    }

    public List<String> listFiles(String remoteDirectory) {

        Comparator<SftpClient.DirEntry> comparator1 = Comparator.comparing(dirEntry -> dirEntry.getAttributes().isDirectory());

        return listFile(remoteDirectory, false)
                .sorted(comparator1.thenComparing(SftpClient.DirEntry::getFilename))
                .map(dirEntry -> dirEntry.getFilename() + "===" + dirEntry.getAttributes().isRegularFile())
                .collect(Collectors.toList());
    }
}
