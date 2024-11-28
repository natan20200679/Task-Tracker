package rmsh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String TASK_DATA_FILE = "task-data.json";
    private static final String NEXT_TASK_ID_FILE = "next-task-id";
    private Path nextTaskIdPath;

    public synchronized boolean saveData(List<Task> tasks) throws IOException {
        
        if(tasks == null) {
            return false;
        }
        
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(TASK_DATA_FILE))) {
            bw.write("[");
        
            for(int i = 0; i < tasks.size(); i++) {
                bw.write("\n\t" + tasks.get(i).toJson() + ( i < (tasks.size() - 1) ? "," : "\n"));
            }
            
            bw.write("]");
            return true;
        }
    }
    
    public List<Task> loadData(String regex) throws FileNotFoundException, IOException {
        List<Task> tasks = new ArrayList<Task>();
        File file = new File(TASK_DATA_FILE);
    
        if(!file.exists()) {
            return tasks;
        }
    
        try(BufferedReader br = new BufferedReader(new FileReader(TASK_DATA_FILE))) {
            String line;
    
            while((line = br.readLine()) != null) {
    
                if(line.matches(regex)) {
                    tasks.add(Task.fromJson(line));
                }
    
            }
        }
    
        return tasks;
    }
    
    public long loadNextTaskId() {
        long nextTaskId = 1;
    
        try {
            nextTaskIdPath = Paths.get(NEXT_TASK_ID_FILE);
            String content = Files.readString(nextTaskIdPath).trim();
            return content.matches("\\d+") ? Long.parseLong(content) : nextTaskId;
        } catch (IOException e) {
            return nextTaskId;
        }
    }
    
    public synchronized void updateNextTaskId() {
    
        try {
            Files.writeString(nextTaskIdPath, String.valueOf(loadNextTaskId() + 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}