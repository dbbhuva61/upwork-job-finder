package com.upwork.example.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.upwork.example.model.UpworkJobSearchData;
import com.upwork.example.service.EmailSenderService;

@Service
public class UpworkDataPollingService {

	@Autowired
	EmailSenderService emailsent;

	@Autowired
	RestTemplate restTemplate;

	public UpworkJobSearchData updataobj;
	
	List<Long> userId = new ArrayList<Long>();

	@Scheduled(fixedRate = 30000)
	public void fatchData() {
			if(updataobj == null) {
				System.out.println("Insert the data");
			return;
			}
		String searchname = updataobj.getSearchquery();
		searchname = searchname.replace(" ", "+");
		String url = "https://www.upwork.com/ab/jobs/search/url?q="+searchname+"&per_page=50&sort=recency";
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add("x-requested-with", "XMLHttpRequest");
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
		headers.add("cookie",
				"visitor_id=206.84.229.13.1653540604376000; spt=a2e0bfec-b9d7-40fe-af48-edec250f27b2; umq=1707; _cq_duid=1.1714459821.EsfPdJl81Wc6qxRR; OptanonAlertBoxClosed=2024-04-30T06:50:29.496Z; _gcl_au=1.1.437699787.1714459917; recognized=e8a3f2f4; company_last_accessed=d1015710629; device_view=full; __cflb=02DiuEXPXZVk436fJfSVuuwDqLqkhavJbAUtmC3LfAZuu; _ga=GA1.1.185245584.1714459821; DA_e8a3f2f4=d192e5897cdd8b10ac3c6cde6208ff3ee655b3ee29a67391f3f7d78333a63ddf; current_organization_uid=1528976591237668865; __cf_bm=TKvPQkc7OzocKuANPgPTrrvBm_j1UhSIwH5H5lpEB6s-1714711293-1.0.1.1-85bt2jk_oME9gCcPpr2oul.N2I82RObD8KTRxpBmRZ50zOyBjvCi8sTpkVWpUGf5mcytAD6oHbmjaN_f6zD7Ag; _cfuvid=rp8ScM0.ec5NHxy5W3Q4Xm5dTFrL54vieFrDlGxBBgU-1714711293579-0.0.1.1-604800000; cf_clearance=ml8J0xN3iFAcCsa8XYHvumJuTujxA28GrMHzgJlUs.c-1714711294-1.0.1.1-OopWLzdb_RGaDW8_WSsfCsj94ru9LG7LAIam9HwToqWAzjtRBl8CoJj7kRGbWdmguWcHg5GonNbW4C4727mQGQ; asct_vt=oauth2v2_f2fe37b2af072ec1cc96bc8241515788; OptanonConsent=isGpcEnabled=0&datestamp=Fri+May+03+2024+10%3A11%3A35+GMT%2B0530+(India+Standard+Time)&version=202305.1.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=c866ffcd-0c4f-4422-8e0d-6f077a421519&interactionCount=1&landingPath=NotLandingPage&groups=C0001%3A1%2CC0002%3A0%2CC0003%3A1%2CC0004%3A0&geolocation=IN%3BGJ&AwaitingReconsent=false; _upw_ses.5831=*; ftr_blst_1h=1714711298368; _cq_suid=1.1714711298.tEvuwmPpMQti8K0f; forterToken=beba3f82b9c543c081d186ba6243ef32_1714711294694__UDF43-m4_14ck; XSRF-TOKEN=df0996e45be38d09080b482a707ada53; console_user=e8a3f2f4; user_uid=1528976591237668864; master_access_token=2307ed00.oauth2v2_6868d9424bfc7521f94f038999683c47; oauth2_global_js_token=oauth2v2_7c0f48558304d5fd02fbd10aae59cee1; _upw_id.5831=b91b5c92-3c73-4f5e-ae19-a86a4a2f727f.1653540606.41.1714711348.1714461555.818f5081-b2a2-4252-a281-b69e260a287d.895bd070-be49-4ebd-9e32-9aacaaef2b63.dffbccac-22aa-4e51-b048-663d5f2ec596.1714711297608.5; country_code=IN; uThemeNull=1; ce29214sb=oauth2v2_53c9ee4d0b48fb87a366262383d47373; _ga_KSM221PNDX=GS1.1.1714711297.2.0.1714711348.0.0.0\n");
		HttpEntity<Object> request = new HttpEntity<Object>(null, headers);
		List<String> prettynamelist = new ArrayList<String>();
		try {
			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
			Map<String, Object> ResponseBody = response.getBody();
			JSONObject jsonResponse = new JSONObject(ResponseBody);
			JSONObject searchResults = jsonResponse.getJSONObject("searchResults");
			JSONArray jobsArray = searchResults.getJSONArray("jobs");
			for (int i = 0; i < jobsArray.length(); i++) {
				JSONObject job = jobsArray.getJSONObject(i);
				JSONArray attrs = job.getJSONArray("attrs");
				for (int j = 0; j < attrs.length(); j++) {
					JSONObject attr = attrs.getJSONObject(j);
					prettynamelist.add(attr.getString("prettyName"));
				}
				if (!updataobj.getExcludekeyword().contains(prettynamelist)) {
					String PublishDate = job.getString("publishedOn");
					DateTimeFormatter DateTimeFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
					ZonedDateTime ZoneDateTime = ZonedDateTime.from(DateTimeFormate.parse(PublishDate));
					long jobtime = ZoneDateTime.toInstant().toEpochMilli();
					long currenttime = System.currentTimeMillis();
					int hours = 24;
					currenttime = currenttime - (((hours * 60) * 60) * 1000);
					if (currenttime < jobtime) {
						JSONObject client = job.getJSONObject("client");
						JSONObject location = client.getJSONObject("location");
						String country = location.getString("country");
						double Budget = 0;
						int fixedBudget = 0;
						 
						if ( "India".equals(location.get("country"))|| country == null) {
							if(job.has("hourlyBudgetText")) {
							String hourlyBudget = job.getString("hourlyBudgetText");
							String[] arrayofBudget = hourlyBudget.split("-", -2);
							String hourlybudget = arrayofBudget[0].replace("$", "");
							Budget = Double.parseDouble(hourlybudget);
							}
							else {
							JSONObject amount = job.getJSONObject("amount");
							fixedBudget = amount.getInt("amount");
							}
							if (5.00 < Budget || 1200 < fixedBudget || Budget <= 0 || fixedBudget <= 0) {
								String title = job.getString("title");
								long userid = job.getLong("uid");
								String description = job.getString("description");
								String duration = job.getString("duration");
								Date date = new Date(jobtime);
								String budget = null;
								if(fixedBudget <=0) {
									budget = job.getString("hourlyBudgetText");	
								}
								else {
									JSONObject amount = job.getJSONObject("amount");
									fixedBudget = amount.getInt("amount");
									budget=String.valueOf(fixedBudget);  
								}
								DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
								String publishtime = format.format(date);
								String jobbody = "\n" + "<html>\n" + "<head>\n"
										+ "    <meta http-equiv=3D\"Content-Type\" content=3D\"text/html; charset=3DUTF-8=\n"
										+ "\">\n"
										+ "    <meta http-equiv=3D\"X-UA-Compatible\" content=3D\"IE=3Dedge\">\n"
										+ "    <meta charset=3D\"utf-8\" name=3D\"viewport\" content=3D\"width=3Ddevice-wid=\n"
										+ "th, initial-scale=3D1.0\">\n" + "    <title>UpWork Job</title>\n"
										+ "    <style class=3D\"text/css\">\n" + "        \n" + "    </style>\n"
										+ "<style>\n" + "\n" + "table {\n" + "  font-family: arial;\n"
										+ "  border-collapse: collapse;\n" + "  width: 100%;\n" + "}\n" + "\n"
										+ "td, th {\n" + "  border: 1px solid #dddddd;\n" + "  text-align: left;\n"
										+ "  padding: 8px;\n" + "}\n" + "\n" + "</style>" + "</head>\n" + "<body>\n"
										+ "<div class=3D\"receipt-ctn-wrapper\">\n"
										+ "    <div class=3D\"receipt-ctn\">\n"
										+ "        <div class=3D\"receipt-header\">\n" + "            </a>\n"
										+ "            <div>\n" + "              <center>\n"
										+ "              			<h2>Upwork Job Details</h2>\n"
										+ "              </center>\n" + "            </div>\n" + "        </div>\n"
										+ "\n" + "        <div class=3D\"receipt-body\">\n"
										+ "            <div style=3D\"text-align:center;line-height:14px\">&nbsp;</div>\n"
										+ "            <div style=3D\"text-align:center;line-height:24px\">\n"
										+ "                <span style=3D\"font-size:18px;font-weight:bold\">\n"
										+ "                    Hi Devang,\n" + "                </span>\n"
										+ "                <br>\n"
										+ "               Your UpWork Job is ready...  hear's the details \n"
										+ "              <br><br>\n" + "                \n" + "<table>\n"
										+ "			  <tr>\n" + "   				<td>Job Title</td>"
										+ "    				<td> " + title + " </td> \n" + "  			</tr>\n"
										+ "  			<tr>\n" + "    				<td>User Id </td>"
										+ "    				<td>" + userid + "</td>\n" + "  			</tr>\n"
										+ "			  <tr>\n" + "   				<td>Description</td>"
										+ "    				<td> " + description + " </td> \n" + "  			</tr>\n"
										+ "			  <tr>\n" + "   				<td>Duration</td>"
										+ "    				<td> " + duration + " </td> \n" + "  			</tr>\n"
										+ "			  <tr>\n" + "   				<td>Hourly Budget</td>"
										+ "    				<td> " +budget+" </td> \n" + "  			</tr>\n"
										+ "			  <tr>\n" + "   				<td>Publish Time</td>"
										+ "    				<td> " + publishtime + " </td> \n" + "  			</tr>\n"
										+ "</table>\n" + "\n" + "</body>\n" + "</html>";
								 	
								try {
									if(!userId.contains(userid)) {
										emailsent.sendEmailWithTemplate(updataobj.getEmailid(), jobbody,"Upwork Job-Springboot");
										userId.add(userid);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						System.out.println("Searching for job.....");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
