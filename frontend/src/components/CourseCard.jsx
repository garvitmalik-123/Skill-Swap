import { Link } from 'react-router-dom'

// The little colored label: Free / SkillPoints / Paid
export function TypeBadge({ course }) {
    if (course.type === 'FREE') {
        return <span className="px-2 py-0.5 rounded-full text-xs bg-green-100 text-green-700">Free</span>
    }
    if (course.type === 'SKILLPOINT') {
        return (
            <span className="px-2 py-0.5 rounded-full text-xs bg-indigo-100 text-indigo-700">
        💎 {course.skillPointCost} SkillPoints
      </span>
        )
    }
    return <span className="px-2 py-0.5 rounded-full text-xs bg-amber-100 text-amber-700">Paid · {course.price}</span>
}

function CourseCard({ course }) {
    return (
        <Link
            to={`/courses/${course.id}`}
            className="block bg-white rounded-xl shadow hover:shadow-md transition p-5 space-y-3"
        >
            {course.thumbnailUrl && (
                <img
                    src={course.thumbnailUrl}
                    alt=""
                    className="w-full h-36 object-cover rounded-lg"
                    onError={(e) => { e.currentTarget.style.display = 'none' }}
                />
            )}
            <div className="flex items-center justify-between">
                <span className="text-xs text-gray-400 uppercase">{course.category}</span>
                <TypeBadge course={course} />
            </div>
            <h3 className="font-bold text-gray-800 text-lg leading-snug">{course.title}</h3>
            <p className="text-sm text-gray-500 line-clamp-2">{course.description}</p>
            <div className="flex items-center justify-between text-xs text-gray-500">
                <span>{course.difficulty}</span>
                <span>⭐ {course.averageRating.toFixed(1)} ({course.reviewCount})</span>
                <span>👥 {course.enrollmentCount}</span>
            </div>
        </Link>
    )
}

export default CourseCard