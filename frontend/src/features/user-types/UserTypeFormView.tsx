import type { FormEvent } from 'react';
import { SubmitButton } from '../../components/common/SubmitButton';
import { TextField } from '../../components/common/TextField';
import type { UserType } from '../../types';
import { field } from '../../utils/forms';

type UserTypeFormViewProps = {
  form: Omit<UserType, 'id'>;
  onChange: (form: Omit<UserType, 'id'>) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
};

export function UserTypeFormView({ form, onChange, onSubmit, saving }: UserTypeFormViewProps) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <TextField label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <label className="input-field">
        <span>Codigo</span>
        <select value={form.code} onChange={(event) => onChange(field(form, 'code', event.target.value as UserType['code']))}>
          <option value="CLIENTE">CLIENTE</option>
          <option value="DONO_RESTAURANTE">DONO_RESTAURANTE</option>
        </select>
      </label>
      <SubmitButton saving={saving} />
    </form>
  );
}
