package com.hdbsoft.nio.file;

import java.io.IOException;
import java.nio.file.*;

public class FileWatchTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("start watch-service..");

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("./data");
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        while(true) {
            //다른방법 => poll(10, TimeUnit.SECONDS);
            //WatchKey key = watchService.poll(); //이용할수 있는 키가 없으면 null반환
            WatchKey key = watchService.take(); //키가 들어올때까지 대기
            for(WatchEvent<?> watchEvent : key.pollEvents()) {
                WatchEvent.Kind<?>  kind = watchEvent.kind();

               if(kind == StandardWatchEventKinds.ENTRY_CREATE) {

               } else if(kind == StandardWatchEventKinds.ENTRY_MODIFY) {

               } else if(kind == StandardWatchEventKinds.ENTRY_DELETE) {

               } else {
                   continue;
               }
               System.out.println(kind);

               WatchEvent<Path> watchEventPath = (WatchEvent<Path>)watchEvent;
               Path fileName = watchEventPath.context();
               System.out.println(fileName);
            }

            //키를 초기화한다.
            boolean valid = key.reset();
            if(valid == false) {
                break; //키가 유효하지 않으면 끝낸다. => ex) 디렉토리가 삭제된 경우.
            }
        }
        watchService.close();
    }

}
