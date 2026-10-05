import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/axios'
import { errorText } from '../api/errorText'
import StatusBadge from '../components/StatusBadge'
import { TypeBadge } from '../components/CourseCard'

function MyCourses() {
    const [courses, setCourses] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')

    useEffect(() => {
        api.get('/me/courses')
            .then((res) => setCourses(res.data.data))
            .catch((err) => setError(errorText(err)))
            .finally(() => setLoading(false))
    }, [])

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-6">
            <div className="max-w-3xl mx-auto space-y-6">
                <div className="flex items-center justify-between">
                    <h1 className="text-3xl font-bold text-indigo-600">My courses</h1>
                    <Link to="/courses/new" className="px-4 py-2 rounded-lg bg-indigo-600 text-white">
                        + Create course
                    </Link>
                </div>

                {loading && <p className="text-gray-600">Loading...</p>}
                {error && <p className="text-red-600">{error}</p>}
                {!loading && !error && courses.length === 0 && (
                    <p className="text-gray-500">You haven't created any courses yet.</p>
                )}

                <div className="space-y-3">
                    {courses.map((c) => (
                        <div key={c.id} className="bg-white rounded-xl shadow p-4 flex items-center justify-between">
                            <div className="space-y-1">
                                <div className="flex items-center gap-2">
                                    <h3 className="font-bold text-gray-800">{c.title}</h3>
                                    <StatusBadge status={c.status} />
                                    <TypeBadge course={c} />
                                </div>
                                <p className="text-sm text-gray-500">👥 {c.enrollmentCount} learners</p>
                            </div>
                            <div className="flex gap-3 text-sm">
                                <Link to={`/courses/${c.id}/manage`} className="text-indigo-600">Manage</Link>
                                <Link to={`/courses/${c.id}`} className="text-gray-500">View</Link>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}

export default MyCourses