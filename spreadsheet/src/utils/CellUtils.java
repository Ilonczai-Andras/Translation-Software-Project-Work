package utils;

public class CellUtils {
    public static int[] parseRef(String ref) {
        String colPart = ref.replaceAll("[0-9]", "");
        int rowPart = Integer.parseInt(ref.replaceAll("[A-Z]", "")) - 1;
        int col = 0;
        for (int i = 0; i < colPart.length(); i++) {
            col = col * 26 + (colPart.charAt(i) - 'A' + 1);
        }
        return new int[]{col - 1, rowPart};
    }

    public static String toRef(int col, int row) {
        StringBuilder sb = new StringBuilder();
        int c = col;
        while (c >= 0) {
            sb.insert(0, (char) ('A' + c % 26));
            c = c / 26 - 1;
        }
        return sb.append(row + 1).toString();
    }
}