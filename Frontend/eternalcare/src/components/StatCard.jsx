const StatCard = ({ label, value, tone = 'primary', detail }) => {
  return (
    <div className="card stat-card h-100 border-0 shadow-sm">
      <div className="card-body">
        <p className={`stat-label text-${tone} mb-2`}>{label}</p>
        <h2 className="display-6 fw-bold mb-1">{value}</h2>
        {detail ? <p className="text-secondary mb-0">{detail}</p> : null}
      </div>
    </div>
  );
};

export default StatCard;