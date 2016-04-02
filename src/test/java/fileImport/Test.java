package fileImport;

public class Test {

	public static void main(String[] args){
//		String s ="[CABBAGE MICHAEL         HQ      de1d66bb05294eb347fd1e5858aa1145       04/03/14        11/06/08        YES, CADWELL SHARON  M       KSC     82708b8ff82146e44d7559b729be2cdc       04/03/14        12/16/08        YES, CAFFREY ROBERT  T       GSFC    9d900dbe9dfe0519843ab02a128f0dd1       04/03/14        11/06/08        YES, CAGEAO  RICHARD P       LaRC    77f8bcb73f3e2e4e4e44e982d912cefc       04/03/14        04/23/10        YES, CAIRES  SAMUEL          ARC     b8c33b4feecc4e29c75fad1cfa5f9dd0        04/03/14        12/31/08        YES/DoD, CALFEE  CHRISTOPHER     H       MSFC    5967502ffa36c92303d57cf3cbe1dcff 09/30/15        07/20/12        YES, CALHOUN JOHN    R       MSFC    9deb5d2e96d80ed4e2b4a54c4b2f66f8 04/03/14        12/31/08        YES, CALLE   LUZ     M       KSC     c8270584db3cac4133f24350add47c51    04/03/14        06/18/10        YES, CALVERT MARTY   R       MSFC    9368b657ff2ff029c30346455d00b9dd        04/03/14        11/20/09        YES, CAMACHO ERNESTO T       KSC     0fc0e33a06682cd61a144638212ff801      04/03/14        12/16/08        YES]";
//		String[] ss = s.split("\\s+",-1);
//		for(String a:ss){
//			System.out.println(a);
//		}
		
		//String s ="1111*a**mdfds*a**dsds*a**dfds";
//		String s ="111\\*\\mdfds\\*\\dsds\\*\\dfds";
//		String b = "\\\\\\*\\\\";
//		String[] ss = s.split(b,-1);
//		for(String a:ss){
//			System.out.println(a);
//		}
		
//		String separator = "\\\\*.\\";
//		if(separator.indexOf("\\")!=-1){
//			  separator = separator.replace("\\", "\\\\");
//		  }
//		  if(separator.indexOf("|")!=-1){
//			  separator = separator.replace("|", "\\|");
//		  }
//		  if(separator.indexOf("[")!=-1){
//			  separator = separator.replace("[", "\\[");
//		  }
//		  if(separator.indexOf("]")!=-1){
//			  separator = separator.replace("[", "\\]");
//		  }		  
//		  if(separator.indexOf(".")!=-1){
//			  separator = separator.replace(".", "[.]");
//		  }
//		  if(separator.indexOf("*")!=-1){
//			  separator = separator.replace("*", "\\*");
//		  }
//		  
//		  String aa ="a\\\\*.\\b\\\\*.\\c\\\\*.\\d";
//		  System.out.println(separator);
//		  System.out.println(aa.split(separator,-1));
		
		String separator="\\s+";
		String bb="CABBAGE MICHAEL         HQ      de1d66bb05294eb347fd1e5858aa1145       04/03/14        11/06/08        YES";
		String[] ss =(bb.split(separator,-1));
		for(String a:ss){
		System.out.println(a);
	}

	}
}
