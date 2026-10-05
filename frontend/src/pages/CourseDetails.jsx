import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import api from '../api/axios'
import { useAuth } from '../context/AuthContext'
import { TypeBadge } from '../components/CourseCard'

function ListCard({ title, items }) {
    if (items.length === 0) return null
    return (
        <div className="bg-white rounded-xl shadow p-6">
            <h2 className="font-bold text-indigo-600 mb-2">{title}</h2>
            <ul className="list-disc pl-5 space-y-1 text-gray-600">
                {items.map((t, i) => <li key={i}>{t}</li>)}
            </ul>
        </div>
    )
}

function CourseDetails() {
    const { id } = useParams() // the course id from the web address
    const { user } = useAuth()

    const [course, setCourse] = useState(null)
    const [lessons, setLessons] = useState([])
    const [creator, setCreator] = useState(null)
    const [enrollment, setEnrollment] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [enrolling, setEnrolling] = useState(false)
    const [enrollError, setEnrollError] = useState('')

    // load the course (plus lessons and teacher name)
    useEffect(() => {
        async function loadCourse() {
            try {
                const res = await api.get(`/courses/${id}`)
                const c = res.data.data
                setCourse(c)

                // extras: if these fail, the page still works
                try {
                    const l = await api.get(`/courses/${id}/lessons`)
                    setLessons(l.data)
                } catch { /* ignore */ }
                try {
                    const u = await api.get(`/users/${c.creatorId}`)
                    setCreator(u.data)
                } catch { /* ignore */ }
            } catch (err) {
                setError(err.response?.data?.message || 'Could not load this course.')
            } finally {
                setLoading(false)
            }
        }
        loadCourse()
    }, [id])

    // if logged in, check whether I am already enrolled
    useEffect(() => {
        if (!user) return
        api.get(`/courses/${id}/enrollment`)
            .then((res) => setEnrollment(res.data.data))
            .catch(() => setEnrollment(null)) // 404 simply means "not enrolled yet"
    }, [id, user])

    async function handleEnroll() {
        setEnrollError('')
        setEnrolling(true)
        try {
            const res = await api.post(`/courses/${id}/enroll`)
            setEnrollment(res.data.data)
            setCourse((c) => ({ ...c, enrollmentCount: c.enrollmentCount + 1 }))
        } catch (err) {
            setEnrollError(err.response?.data?.message || 'Could not enroll. Please try again.')
        } finally {
            setEnrolling(false)
        }
    }

    if (loading) return <p className="p-8 text-gray-600">Loading course...</p>
    if (error) return <p className="p-8 text-red-600">{error}</p>

    function priceText() {
        if (course.type === 'FREE') return 'Free'
        if (course.type === 'SKILLPOINT') return `💎 ${course.skillPointCost} SkillPoints`
        return `${course.price}`
    }

    function enrollLabel() {
        if (course.type === 'FREE') return 'Enroll for free'
        if (course.type === 'SKILLPOINT') return `Enroll with ${course.skillPointCost} SkillPoints`
        return `Buy for ${course.price}`
    }

    // decides what appears in the box on the right
    function renderAction() {
        if (course.status !== 'PUBLISHED') {
            return <p className="text-amber-600 text-sm">This course is not published yet.</p>
        }
        if (!user) {
            return (
                <Link to="/login" className="block text-center px-4 py-2 rounded-lg bg-indigo-600 text-white">
                    Log in to enroll
                </Link>
            )
        }
        if (enrollment) {
            return (
                <div className="rounded-lg bg-green-50 text-green-700 p-3 text-sm">
                    ✅ You are enrolled ({enrollment.status})
                </div>
            )
        }
        return (
            <>
                {enrollError && <p className="text-red-600 text-sm">{enrollError}</p>}
                <button
                    onClick={handleEnroll}
                    disabled={enrolling}
                    className="w-full px-4 py-2 rounded-lg bg-indigo-600 text-white disabled:opacity-50"
                >
                    {enrolling ? 'Enrolling...' : enrollLabel()}
                </button>
            </>
        )
    }

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-6">
            <div className="max-w-5xl mx-auto">
                <Link to="/courses" className="text-sm text-indigo-600">← Back to courses</Link>

                <div className="grid lg:grid-cols-3 gap-6 mt-4">
                    {/* Left side: the details */}
                    <div className="lg:col-span-2 space-y-6">
                        <div className="bg-white rounded-xl shadow p-6 space-y-3">
                            <div className="flex flex-wrap items-center gap-2">
                                <span className="text-xs text-gray-400 uppercase">{course.category}</span>
                                <TypeBadge course={course} />
                                <span className="text-xs text-gray-500">{course.difficulty}</span>
                            </div>
                            <h1 className="text-3xl font-bold text-gray-800">{course.title}</h1>
                            {creator && (
                                <p className="text-gray-500">
                                    Taught by <span className="font-medium text-gray-700">{creator.name}</span>
                                </p>
                            )}
                            <p className="text-gray-600 whitespace-pre-line">{course.description}</p>
                            <div className="flex flex-wrap gap-4 text-sm text-gray-500">
                                <span>⭐ {course.averageRating.toFixed(1)} ({course.reviewCount} reviews)</span>
                                <span>👥 {course.enrollmentCount} learners</span>
                                {course.durationMinutes && <span>⏱ {course.durationMinutes} min</span>}
                                {course.language && <span>🌐 {course.language}</span>}
                            </div>
                        </div>

                        <ListCard title="What you will learn" items={course.learningObjectives ?? []} />
                        <ListCard title="Before you start" items={course.prerequisites ?? []} />

                        {(course.skills ?? []).length > 0 && (
                            <div className="bg-white rounded-xl shadow p-6">
                                <h2 className="font-bold text-indigo-600 mb-2">Skills covered</h2>
                                <div className="flex flex-wrap gap-2">
                                    {course.skills.map((s) => (
                                        <span key={s} className="bg-indigo-50 text-indigo-700 rounded-full px-3 py-1 text-sm">{s}</span>
                                    ))}
                                </div>
                            </div>
                        )}

                        <div className="bg-white rounded-xl shadow p-6">
                            <h2 className="font-bold text-indigo-600 mb-2">Lessons</h2>
                            {lessons.length === 0 ? (
                                <p className="text-gray-400 text-sm">No lessons published yet.</p>
                            ) : (
                                <ol className="space-y-2">
                                    {lessons.map((l, i) => (
                                        <li key={l.id} className="flex justify-between text-gray-600 border-b pb-2 last:border-0">
                                            <span>{i + 1}. {l.title}</span>
                                            {l.durationMinutes && <span className="text-sm text-gray-400">{l.durationMinutes} min</span>}
                                        </li>
                                    ))}
                                </ol>
                            )}
                        </div>
                    </div>

                    {/* Right side: price and enroll */}
                    <div className="bg-white rounded-xl shadow p-6 space-y-4 h-fit">
                        <div className="text-2xl font-bold text-gray-800">{priceText()}</div>
                        {renderAction()}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CourseDetails