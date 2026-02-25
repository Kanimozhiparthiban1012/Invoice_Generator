package in.kanimozhi.invoicegeneratorapi.service;

import in.kanimozhi.invoicegeneratorapi.entity.Invoice;
import org.springframework.stereotype.Service;
import in.kanimozhi.invoicegeneratorapi.repository.InvoiceRepository;
import java.util.List;
@Service

public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice saveInvoice(Invoice invoice){
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> fetchInvoices(String clerkId){
        return invoiceRepository.findByClerkId(clerkId);
    }

    public void removeInvoice(String invoiceId, String clerkId){
        Invoice existingInvoice = invoiceRepository.findByClerkIdAndId(clerkId, invoiceId)
                .orElseThrow(() -> new RuntimeException(("Invoice not found: "+invoiceId)));
        invoiceRepository.delete(existingInvoice);
    }

}
