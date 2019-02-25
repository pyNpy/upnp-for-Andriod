package org.fourthline.cling.binding.xml;

public class XmlDescriptFixtor {
	static private String testString=
       		"<root xmlns=\"urn:schemas-upnp-org:device-1-0\" xmlns:dlna=\"urn:schemas-dlna-org:device-1-0\">\r\n" + 
       		"    <dlna:X_DLNADOC xmlns:dlna=\"urn:schemas-dlna-org:device-1-0\">DMR-1.50</dlna:X_DLNADOC>\r\n" + 
       		"</root>"
       		; 	

	public static void main(String[] args) {
		 xmlFix(testString);

	}
 	

	/**
	 * `<dlna:X_DLNADOC xmlns:dlna="urn:schemas-dlna-org:device-1-0">DMR-1.50</dlna:X_DLNADOC>`
	 *to fix xml security :xml Entity attack for xml Document 
	 */
	static public String xmlFix(String descriptxml)
	{
		System.out.println(descriptxml);
		
		int index_0 =descriptxml.indexOf("<dlna:X_DLNADOC");
		int index_1 =descriptxml.indexOf("</dlna:X_DLNADOC>");
		
		if(index_0==-1)
			return descriptxml;//do nothing
		
		String  preword =descriptxml.substring(0, index_0);
		String  sufword =descriptxml.substring(index_1 + "</dlna:X_DLNADOC>".length());
		String  problemword= descriptxml.substring(index_0 , index_1+"</dlna:X_DLNADOC>".length());
		//this words may cause xml security
//		System.out.println(problemword);
		String dString= problemword.substring(problemword.indexOf('>')+1,problemword.lastIndexOf("</"));
//		System.out.println(dString);
		String fixedStr="<dlna>"+dString.trim()+"</dlna>";
//		System.out.println(fixedStr);
		
//		System.out.println(preword+fixedStr+sufword);
		return preword+fixedStr+sufword;
		
	}
	 
 
}
