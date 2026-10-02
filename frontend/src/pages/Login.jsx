import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function Login() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()
    const { login } = useAuth()

    async function handleSubmit(e) {
        e.preventDefault()
        setError('')
        setLoading(true)
        try {
            await login(email, password)
            navigate('/dashboard')
        } catch (err) {
            setError(err.response?.data?.message || err.message || 'Something went wrong. Is the backend running?')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-indigo-50">
            <form onSubmit={handleSubmit} className="w-full max-w-sm bg-white p-8 rounded-xl shadow space-y-4">
                <h1 className="text-2xl font-bold text-indigo-600 text-center">Welcome back</h1>

                {error && <p className="text-red-600 text-sm">{error}</p>}

                <input
                    className="w-full border rounded-lg px-3 py-2"
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    className="w-full border rounded-lg px-3 py-2"
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button
                    disabled={loading}
                    className="w-full py-2 rounded-lg bg-indigo-600 text-white disabled:opacity-50"
                >
                    {loading ? 'Logging in...' : 'Login'}
                </button>

                <p className="text-sm text-center text-gray-600">
                    New here? <Link to="/register" className="text-indigo-600">Create an account</Link>
                </p>
            </form>
        </div>
    )
}

export default Login