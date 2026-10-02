import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

function Dashboard() {
    const [me, setMe] = useState(null)
    const [error, setError] = useState('')
    const navigate = useNavigate()

    // runs once when the page opens
    useEffect(() => {
        api.get('/auth/me')
            .then((res) => setMe(res.data))
            .catch(() => {
                setError('Could not load your account. Please log in again.')
            })
    }, [])

    function logout() {
        localStorage.removeItem('token')
        navigate('/login')
    }

    return (
        <div className="min-h-screen bg-indigo-50 p-8">
            <div className="max-w-xl mx-auto bg-white p-6 rounded-xl shadow space-y-4">
                <h1 className="text-2xl font-bold text-indigo-600">Dashboard 🎉</h1>
                {error && <p className="text-red-600">{error}</p>}
                {me && (
                    <pre className="bg-gray-100 p-3 rounded text-sm overflow-auto">
            {JSON.stringify(me, null, 2)}
          </pre>
                )}
                <button onClick={logout} className="px-4 py-2 rounded-lg bg-indigo-600 text-white">
                    Logout
                </button>
            </div>
        </div>
    )
}

export default Dashboard