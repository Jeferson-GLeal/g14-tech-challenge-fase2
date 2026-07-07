import type { InputHTMLAttributes } from 'react';

type TextFieldProps = {
  disabled?: boolean;
  label: string;
  onChange: (value: string) => void;
  value: string;
} & Omit<InputHTMLAttributes<HTMLInputElement>, 'onChange' | 'value'>;

export function TextField({ disabled, label, onChange, value, ...props }: TextFieldProps) {
  return (
    <label className="input-field">
      <span>{label}</span>
      <input disabled={disabled} value={value} onChange={(event) => onChange(event.target.value)} {...props} />
    </label>
  );
}
