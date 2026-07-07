type ConfirmModalProps = {
  confirmLabel?: string;
  description: string;
  loading?: boolean;
  onCancel: () => void;
  onConfirm: () => void;
  open: boolean;
  title: string;
};

export function ConfirmModal({
  confirmLabel = 'Confirmar',
  description,
  loading,
  onCancel,
  onConfirm,
  open,
  title,
}: ConfirmModalProps) {
  if (!open) return null;

  return (
    <div className="modal-backdrop" role="presentation">
      <section className="modal" role="dialog" aria-modal="true" aria-labelledby="confirm-title">
        <div className="modal-header">
          <h2 id="confirm-title">{title}</h2>
          <button className="modal-close" disabled={loading} onClick={onCancel} type="button" aria-label="Fechar modal">
            x
          </button>
        </div>
        <p>{description}</p>
        <div className="modal-actions">
          <button className="secondary-button" disabled={loading} onClick={onCancel} type="button">
            Cancelar
          </button>
          <button className="danger-button" disabled={loading} onClick={onConfirm} type="button">
            {loading ? 'Removendo...' : confirmLabel}
          </button>
        </div>
      </section>
    </div>
  );
}
