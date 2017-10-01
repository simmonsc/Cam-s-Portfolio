package lab08;

public class MakingTests {

	public static void makeInput(){
		for (int i = 0; i< 9;i++){
			System.out.print((char)9);
		}
		System.out.println();
		for (int i=32;i<127;i++){
			
			for (int j =0;j<i;j++){
				System.out.print((char)i);
			}
			System.out.println();
		}
	}
	
	public static void makeOutput(){
		System.out.println("\\t:9");
		System.out.println("\\n:"+ (126-32+1));
		for (int i =32; i< 127;i++){
			System.out.print((char)i);
			System.out.println(":"+ i);
		}
	}
	
	public static void main(String[] args) {
		//makeInput();
		makeOutput();
	}

}
