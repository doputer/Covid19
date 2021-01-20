package sejong.corona;

public class UserDTO {
	private int id;
	private String name; // 이름
	private String phone; // 연락처
	private String address; // 주소
	private String birth; // 생년월일
	private String gender; // 성별
	private String symptom1; // 증상1
	private String symptom2; // 증상2
	private String symptom3; // 증상3
	private String symptom4; // 증상4
	private String etc; // 기타 증상
	private String hospital; // 선별 진료소
	private String date; // 예약 일자
	private String status; // 예약 상태(예약 대기, 예약 완료)
	private String result; // 관리자 진단 결과

	public UserDTO() {
	}

	public UserDTO(int id) {
		this.id = id;
	}

	public UserDTO(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}

	public UserDTO(String hospital, String date, String status) {
		this.hospital = hospital;
		this.date = date;
		this.status = status;
	}

	public UserDTO(String name, String hospital, String date, String status) {
		this.name = name;
		this.hospital = hospital;
		this.date = date;
		this.status = status;
	}

	public UserDTO(int id, String address, String birth, String gender) {
		this.id = id;
		this.address = address;
		this.birth = birth;
		this.gender = gender;
	}

	public UserDTO(String symptom1, String symptom2, String symptom3, String symptom4, String etc) {
		this.symptom1 = symptom1;
		this.symptom2 = symptom2;
		this.symptom3 = symptom3;
		this.symptom4 = symptom4;
		this.etc = etc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSymptom1() {
		return symptom1;
	}

	public void setSymptom1(String symptom1) {
		this.symptom1 = symptom1;
	}

	public String getSymptom2() {
		return symptom2;
	}

	public void setSymptom2(String symptom2) {
		this.symptom2 = symptom2;
	}

	public String getSymptom3() {
		return symptom3;
	}

	public void setSymptom3(String symptom3) {
		this.symptom3 = symptom3;
	}

	public String getSymptom4() {
		return symptom4;
	}

	public void setSymptom4(String symptom4) {
		this.symptom4 = symptom4;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}