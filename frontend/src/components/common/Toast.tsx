export type ToastMessage = {
  id: number;
  kind: 'success' | 'danger';
  message: string;
};

type ToastProps = {
  toast?: ToastMessage;
  onClose: () => void;
};

export function Toast({ onClose, toast }: ToastProps) {
  if (!toast) return null;

  return (
    <div className="toast-region" aria-live="polite" aria-atomic="true">
      <div className={`toast ${toast.kind}`}>
        <div>
          <strong>{toast.kind === 'success' ? 'Tudo certo' : 'Atencao'}</strong>
          <p>{toast.message}</p>
        </div>
        <button className="toast-close" onClick={onClose} type="button" aria-label="Fechar aviso">
          x
        </button>
      </div>
    </div>
  );
}
