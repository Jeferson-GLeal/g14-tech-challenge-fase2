type SubmitButtonProps = {
  disabled?: boolean;
  saving: boolean;
};

export function SubmitButton({ disabled, saving }: SubmitButtonProps) {
  return (
    <button className="primary-button" disabled={disabled || saving} type="submit">
      {saving ? 'Salvando...' : 'Salvar'}
    </button>
  );
}
