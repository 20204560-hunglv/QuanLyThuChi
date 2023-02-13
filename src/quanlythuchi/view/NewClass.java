package quanlythuchi.view;

import java.text.NumberFormat;

public class NewClass {
    private String formatNBer(int number){
        NumberFormat currentLocale = NumberFormat.getInstance();
		
	// định dạng số của khu vực mặc định của máy ảo JVM
	// sử dụng phương thức format()
	return currentLocale.format(number);
    }
    public static void main(String[] args) {
        NewClass newClass = new NewClass();
        System.out.println(newClass.formatNBer(123456789)+" đ");
    }
}
