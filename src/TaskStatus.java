import java.util.Arrays;

public enum TaskStatus {
    TODO, IN_PROGRESS, DONE;

    public static boolean isValid(String status) {
        
        try {
            TaskStatus.valueOf(valueToName(status));
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static TaskStatus fromValue(String status) {
        return TaskStatus.valueOf(valueToName(status));
    }
    
    @Override
    public String toString() {
        return name().replace("_", "-").toLowerCase();
    }
    
    public static String valuesJoin(String delimiter) {
        return String.join(delimiter, Arrays.stream(TaskStatus.values()).map(Enum::toString)
        .toArray(String[]::new));
    }
    
    private static String valueToName(String status) {
        return status.replaceAll("-", "_").toUpperCase();
    }
}