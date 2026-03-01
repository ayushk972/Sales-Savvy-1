package com.example.demo.admincontroller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.adminservice.AdminbusinessService;

@RestController
@RequestMapping("/admin/business")
public class AdminBusinessController {

	private final AdminbusinessService adminBusinessService;
	
	public AdminBusinessController(AdminbusinessService adminbusinessService) {
		this.adminBusinessService = adminbusinessService;
	}
	
	@GetMapping("/monthly") 
	public ResponseEntity<?> getMonthlyBusniess(@RequestParam int month, @RequestParam int year) {
		try {
			Map<String, Object> businessReport = adminBusinessService.calculateMonthlyBusiness(month, year);
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
		
	}
	
	@GetMapping("/daily")
	public ResponseEntity<?> getDailyBusiness(@RequestParam String date) {
		try {
			LocalDate localDate = LocalDate.parse(date);
			Map<String, Object> businessReport = adminBusinessService.calculateDailyBusiness(localDate);
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wromg");
		}
	}
	
	@GetMapping("/yearly")
	public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {
		try {
			Map<String, Object> businessReport = adminBusinessService.calculateYearlyBusiness(year);
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
			
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}
	
	
	@GetMapping("/overall")
	public ResponseEntity<?> getOverallBusiness() {
		try {
			Map<String, Object> overallBusiness = adminBusinessService.calculateOverallBusiness();
			return ResponseEntity.status(HttpStatus.OK).body(overallBusiness);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while calculating overall business");
		}
	}
}
















