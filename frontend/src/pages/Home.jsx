import { Link } from 'react-router-dom'

function Home() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center gap-6 bg-indigo-50">
            <h1 className="text-4xl font-bold text-indigo-600">🤝 SkillSwap</h1>
            <p className="text-gray-600">Teach what you know. Learn what you want.</p>
            <div className="flex gap-4">
                <Link to="/login" className="px-5 py-2 rounded-lg bg-indigo-600 text-white">
                    Login
                </Link>
                <Link to="/register" className="px-5 py-2 rounded-lg border border-indigo-600 text-indigo-600">
                    Register
                </Link>
            </div>
        </div>
    )
}

export default Home