// invoiceService.js
import axios from "axios";

export const saveInvoice = (baseURL, payload, token) => {
    return axios.post(`${baseURL}/invoices`, payload, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

export const getAllInvoices = (baseURL, token) => {
    return axios.get(`${baseURL}/invoices`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

export const deleteInvoice = (baseURL, id, token) => {
    return axios.delete(`${baseURL}/invoices/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

export const sendInvoice = (baseURL, file, customerEmail, token) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("email", customerEmail);

    return axios.post(`${baseURL}/invoices/sendinvoice`, formData, {
        headers: {
            Authorization: `Bearer ${token}`, // JWT for security
            // Do NOT manually set Content-Type; Axios sets multipart boundaries automatically
        },
    });
};