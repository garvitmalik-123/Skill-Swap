import { useEffect, useState } from 'react'
import api from '../api/axios'
import CourseCard from '../components/CourseCard'

const TYPES = [
    ['FREE', 'Free'],
    ['SKILLPOINT', 'SkillPoints'],
    ['PAID', 'Paid'],
]
const DIFFICULTIES = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED']

function Courses() {
    const [categories, setCategories] = useState([])

    // what the user is typing in the search bar
    const [keyword, setKeyword] = useState('')
    const [category, setCategory] = useState('')
    const [type, setType] = useState('')
    const [difficulty, setDifficulty] = useState('')

    // what was actually searched (changes only when Search is clicked)
    const [filters, setFilters] = useState({})
    const [page, setPage] = useState(0)

    const [courses, setCourses] = useState([])
    const [totalPages, setTotalPages] = useState(0)
    const [totalElements, setTotalElements] = useState(0)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')

    // load the category names for the dropdown (once)
    useEffect(() => {
        api.get('/categories')
            .then((res) => setCategories(res.data))
            .catch(() => {})
    }, [])

    // load courses whenever the filters or the page number change
    useEffect(() => {
        async function load() {
            setLoading(true)
            setError('')
            try {
                const params = { page, size: 9, ...filters }
                const res = await api.get('/courses/search', { params })
                const result = res.data.data
                setCourses(result.content)
                setTotalPages(result.totalPages)
                setTotalElements(result.totalElements)
            } catch (err) {
                setError(err.response?.data?.message || 'Could not load courses. Is the backend running?')
            } finally {
                setLoading(false)
            }
        }
        load()
    }, [filters, page])

    function handleSearch(e) {
        e.preventDefault()
        const next = {}
        if (keyword.trim()) next.keyword = keyword.trim()
        if (category) next.category = category
        if (type) next.type = type
        if (difficulty) next.difficulty = difficulty
        setPage(0)
        setFilters(next)
    }

    function handleClear() {
        setKeyword('')
        setCategory('')
        setType('')
        setDifficulty('')
        setPage(0)
        setFilters({})
    }

    return (
        <div className="min-h-[calc(100vh-64px)] bg-indigo-50 p-6">
            <div className="max-w-6xl mx-auto space-y-6">
                <h1 className="text-3xl font-bold text-indigo-600">Explore courses</h1>

                {/* Search bar */}
                <form onSubmit={handleSearch} className="bg-white rounded-xl shadow p-4 grid md:grid-cols-5 gap-3">
                    <input
                        className="border rounded-lg px-3 py-2 md:col-span-2"
                        placeholder="Search by title or description..."
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                    />
                    <select className="border rounded-lg px-3 py-2" value={category} onChange={(e) => setCategory(e.target.value)}>
                        <option value="">All categories</option>
                        {categories.map((c) => (
                            <option key={c.id} value={c.name}>{c.name}</option>
                        ))}
                    </select>
                    <select className="border rounded-lg px-3 py-2" value={type} onChange={(e) => setType(e.target.value)}>
                        <option value="">Any price</option>
                        {TYPES.map(([value, label]) => (
                            <option key={value} value={value}>{label}</option>
                        ))}
                    </select>
                    <select className="border rounded-lg px-3 py-2" value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
                        <option value="">Any level</option>
                        {DIFFICULTIES.map((d) => (
                            <option key={d} value={d}>{d}</option>
                        ))}
                    </select>

                    <div className="md:col-span-5 flex gap-3">
                        <button className="px-5 py-2 rounded-lg bg-indigo-600 text-white">Search</button>
                        <button type="button" onClick={handleClear} className="px-5 py-2 rounded-lg border border-indigo-600 text-indigo-600">
                            Clear
                        </button>
                    </div>
                </form>

                {/* Results */}
                {error && <p className="text-red-600">{error}</p>}
                {loading && <p className="text-gray-600">Loading courses...</p>}

                {!loading && !error && courses.length === 0 && (
                    <p className="text-gray-500">No courses found. Try different filters.</p>
                )}

                {!loading && courses.length > 0 && (
                    <>
                        <p className="text-sm text-gray-500">{totalElements} course(s) found</p>
                        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-5">
                            {courses.map((c) => (
                                <CourseCard key={c.id} course={c} />
                            ))}
                        </div>

                        {/* Page buttons */}
                        <div className="flex items-center justify-center gap-4">
                            <button
                                disabled={page === 0}
                                onClick={() => setPage(page - 1)}
                                className="px-4 py-2 rounded-lg bg-white shadow disabled:opacity-40"
                            >
                                ← Previous
                            </button>
                            <span className="text-sm text-gray-600">Page {page + 1} of {totalPages}</span>
                            <button
                                disabled={page + 1 >= totalPages}
                                onClick={() => setPage(page + 1)}
                                className="px-4 py-2 rounded-lg bg-white shadow disabled:opacity-40"
                            >
                                Next →
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    )
}

export default Courses