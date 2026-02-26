import axios from "axios";

const authHeaders = (token) => {
    if (!token) {
        throw new Error("JWT token missing");
    }
    return { Authorization: `Bearer ${token}` };
};

export const saveInvoice = (baseURL, payload, token) => {
    return axios.post(`${baseURL}/invoices`, payload, {
        headers: authHeaders(token),
    });
};

export const getAllInvoices = (baseURL, token) => {
    return axios.get(`${baseURL}/invoices`, {
        headers: authHeaders(token),
    });
};

export const deleteInvoice = (baseURL, id, token) => {
    return axios.delete(`${baseURL}/invoices/${id}`, {
        headers: authHeaders(token),
    });
};

export const sendInvoice = (baseURL, file, customerEmail, token) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("email", customerEmail);

    return axios.post(`${baseURL}/invoices/sendinvoice`, formData, {
        headers: authHeaders(token),
    });
};