import axios from 'axios'

// Every request will start with this address (your Spring Boot backend)
const api = axios.create({
    baseURL: 'http://localhost:8080/api',
})

// Before EVERY request leaves, check: is the user logged in?
// If yes, attach their JWT token like an ID card.
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

export default api