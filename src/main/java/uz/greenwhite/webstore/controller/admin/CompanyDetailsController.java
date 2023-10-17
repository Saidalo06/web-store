package uz.greenwhite.webstore.controller.admin;

import lombok.AllArgsConstructor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import uz.greenwhite.webstore.entity.CompanyDetails;
import uz.greenwhite.webstore.service.CompanyDetailsService;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/data/companyDetails")
public class CompanyDetailsController {
    private final CompanyDetailsService service;
    private final String FILE_ROOT = "resources/static/FILE";
    
    @GetMapping()
    public String listPage(Model model, Pageable pageable) {
         CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
         model.addAttribute("companyDetails", companyDetails);
         return "/admin/data/companyDetails/add";
    }
   
    @GetMapping("/edit")
    public String editPage(Model model) {
       CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
       model.addAttribute("companyDetails", companyDetails);
       return "/admin/data/companyDetails/add";
    }
    
    @PostMapping("/edit")
    public String editCompanyDetails(@ModelAttribute CompanyDetails companyDetails, @RequestParam("file") MultipartFile file) throws IOException {
        CompanyDetails existingCompanyDetails = service.getById(1L); // Retrieve the existing CompanyDetails entity with ID 1
        companyDetails.setCompanyDetailsId(existingCompanyDetails.getCompanyDetailsId()); // Set the ID to 1
        companyDetails = service.update(companyDetails);
       saveFile(companyDetails, file);
        return "redirect:/admin/data/companyDetails";
    }
  
    @GetMapping("/image")
    public void image(HttpServletResponse response) {
        CompanyDetails companyDetails = service.getById(1L); // Retrieve the CompanyDetails entity with ID 1
        File file = new File(FILE_ROOT + "/companyDetails/" + companyDetails.getLogoPath());
        // Rest of the method implementation remains unchanged
            if (file.exists()) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("inline; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerValue);
            FileInputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
                try {
                    int c;
                    while ((c = inputStream.read()) != -1) {
                        response.getWriter().write(c);
                    }
                } finally {
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    response.getWriter().close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            throw new RuntimeException("File not exist");
        }

    }
   
    public void saveFile(CompanyDetails companyDetails, MultipartFile file)throws IOException{
        
      new File(FILE_ROOT+"/companyDetails").mkdir();

        String fileName=file.getOriginalFilename();
        String ext="";
        if(fileName!=null&&fileName.contains(".")){
             ext=fileName.substring(fileName.lastIndexOf("."));
        }
        File sf=new File(FILE_ROOT +"/companyDetails/"+companyDetails.getCompanyDetailsId()+ext);
        FileOutputStream fos = new FileOutputStream(sf);
        BufferedOutputStream bw = new BufferedOutputStream(fos);

        bw.write(file.getBytes());
        bw.close();
        fos.close();

        companyDetails.setLogoPath(sf.getName());
        service.update(companyDetails);
    }
}