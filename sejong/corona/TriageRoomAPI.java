package sejong.corona;

import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;

public class TriageRoomAPI extends Thread {
	static Vector<String> v = new Vector<String>();
	static HashSet<String> h = new HashSet<String>();
	StringBuilder urlBuilder;

	TriageRoomAPI() throws IOException {
		urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/pubReliefHospService/getpubReliefHospList"); /*URL*/
	    urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=FGCYsRMVV8zUAjWgMk%2BREtpmO%2F5HJpxRq%2F1R7Z5%2B40UPUz6s6Q3K%2Bb0Eb68cUBuxOz%2FyD%2Fa17jxNHgpQxxn6Pg%3D%3D"); /*Service Key*/
	    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
	    urlBuilder.append("&" + URLEncoder.encode("spclAdmTyCd","UTF-8") + "=" + URLEncoder.encode("99", "UTF-8")); /*A0: 국민안심병원/97: 코로나검사 실시기관/99: 코로나 선별진료소 운영기관*/

		this.start();
	}
	
	public Vector<String> getTriageRoom() {
		return v;
	}
	
	@Override
	public void run() {
		int page = 1; // 페이지 초기값
		try {
			while (true) {
				String url = new URL(urlBuilder.toString()).toString();
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);

				// root tag
				doc.getDocumentElement().normalize();

				// 파싱할 tag
				NodeList nList = doc.getElementsByTagName("item");

				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if (getTagValue("sidoNm", eElement).toString().equals("서울")) {
							h.add(getTagValue("yadmNm", eElement).toString());
						}
					}
				}

				page += 1;
				if (page > 1) {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		v = new Vector<String>(h);
		v.sort(String.CASE_INSENSITIVE_ORDER);
		v.add(0, "아산병원");
		
		this.interrupt();
	}

	// tag값의 정보를 가져오는 메소드
	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}
}
