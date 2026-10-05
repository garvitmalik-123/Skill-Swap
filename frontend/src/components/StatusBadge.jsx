function StatusBadge({ status }) {
    const styles = {
        DRAFT: 'bg-amber-100 text-amber-700',
        PUBLISHED: 'bg-green-100 text-green-700',
        ARCHIVED: 'bg-gray-200 text-gray-600',
    }
    return (
        <span className={`px-2 py-0.5 rounded-full text-xs ${styles[status] ?? ''}`}>
      {status}
    </span>
    )
}

export default StatusBadge