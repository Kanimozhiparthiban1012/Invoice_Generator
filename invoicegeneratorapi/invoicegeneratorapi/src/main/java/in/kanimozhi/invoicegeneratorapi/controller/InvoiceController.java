package in.kanimozhi.invoicegeneratorapi.controller;

import in.kanimozhi.invoicegeneratorapi.entity.Invoice;
import in.kanimozhi.invoicegeneratorapi.service.EmailService;
import in.kanimozhi.invoicegeneratorapi.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final EmailService emailService;

    // ================= SAVE INVOICE =================
    @PostMapping
    public ResponseEntity<Invoice> saveInvoice(
            @RequestBody Invoice invoice,
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        invoice.setClerkId(authentication.getName());
        return ResponseEntity.ok(invoiceService.saveInvoice(invoice));
    }

    // ================= FETCH INVOICES =================
    @GetMapping
    public ResponseEntity<List<Invoice>> fetchInvoices(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        return ResponseEntity.ok(
                invoiceService.fetchInvoices(authentication.getName())
        );
    }

    // ================= DELETE INVOICE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeInvoice(
            @PathVariable String id,
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        invoiceService.removeInvoice(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    // ================= SEND INVOICE EMAIL =================
    @PostMapping(
            value = "/sendinvoice",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> sendInvoice(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String customerEmail,
            Authentication authentication
    ) {

        // 🔍 DEBUG (keep for now)
        System.out.println("Reached /sendinvoice endpoint");

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing");
        }

        if (customerEmail == null || !customerEmail.contains("@")) {
            return ResponseEntity.badRequest().body("Invalid email address");
        }

        try {
            String userId = authentication.getName();
            emailService.sendInvoiceEmail(customerEmail, file, userId);
            return ResponseEntity.ok("Invoice sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send invoice");
        }
    }
}