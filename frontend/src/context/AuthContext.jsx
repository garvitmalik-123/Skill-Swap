import { createContext, useContext, useEffect, useState } from 'react'
import api from '../api/axios'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null)
    const [loading, setLoading] = useState(true)

    // Ask the backend "who am I?" using the saved token
    async function loadUser() {
        const token = localStorage.getItem('token')
        if (!token) {
            setUser(null)
            setLoading(false)
            return
        }
        try {
            const res = await api.get('/auth/me')
            setUser(res.data.data)
        } catch {
            localStorage.removeItem('token') // token is old or broken
            setUser(null)
        } finally {
            setLoading(false)
        }
    }

    // runs once when the app opens (so a page refresh keeps you logged in)
    useEffect(() => {
        loadUser()
    }, [])

    async function login(email, password) {
        const res = await api.post('/auth/login', { email, password })
        const body = res.data
        const token =
            body.token || body.accessToken || body.data?.token || body.data?.accessToken
        if (!token) throw new Error('Could not find the token in the login response')
        localStorage.setItem('token', token)
        await loadUser()
    }

    function logout() {
        localStorage.removeItem('token')
        setUser(null)
    }

    return (
        <AuthContext.Provider value={{ user, loading, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    return useContext(AuthContext)
}