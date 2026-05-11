const SectionHeader = ({ eyebrow, title, description }) => {
  return (
    <div className="mb-4">
      {eyebrow ? <p className="section-eyebrow mb-2">{eyebrow}</p> : null}
      <h1 className="display-6 fw-bold mb-2">{title}</h1>
      {description ? <p className="text-secondary mb-0">{description}</p> : null}
    </div>
  );
};

export default SectionHeader;