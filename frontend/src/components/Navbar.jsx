import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function Navbar() {
    const { user, logout } = useAuth()
    const navigate = useNavigate()

    function handleLogout() {
        logout()
        navigate('/login')
    }

    return (
        <nav className="h-16 bg-white shadow flex items-center justify-between px-6">
            <Link to="/" className="text-xl font-bold text-indigo-600">
                🤝 SkillSwap
            </Link>

            {user ? (
                <div className="flex items-center gap-4">
                    <Link to="/dashboard" className="text-gray-700">Dashboard</Link>
                    <span className="text-gray-500">Hi, {user.name}</span>
                    <button onClick={handleLogout} className="px-4 py-1.5 rounded-lg bg-indigo-600 text-white">
                        Logout
                    </button>
                </div>
            ) : (
                <div className="flex items-center gap-4">
                    <Link to="/login" className="text-gray-700">Login</Link>
                    <Link to="/register" className="px-4 py-1.5 rounded-lg bg-indigo-600 text-white">
                        Register
                    </Link>
                </div>
            )}
        </nav>
    )
}

export default Navbar