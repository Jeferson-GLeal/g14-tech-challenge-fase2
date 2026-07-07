import type { FormEvent } from 'react';
import { AddressFields } from '../../components/common/AddressFields';
import { SubmitButton } from '../../components/common/SubmitButton';
import { TextField } from '../../components/common/TextField';
import type { Mode, UserForm, UserType } from '../../types';
import { field } from '../../utils/forms';

type UserFormViewProps = {
  form: UserForm;
  mode: Mode;
  onChange: (form: UserForm) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
  types: UserType[];
};

export function UserFormView({ form, mode, onChange, onSubmit, saving, types }: UserFormViewProps) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <TextField label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <TextField label="Email" type="email" value={form.email} onChange={(value) => onChange(field(form, 'email', value))} />
      <TextField label="Login" value={form.login} onChange={(value) => onChange(field(form, 'login', value))} />
      {mode === 'create' && (
        <TextField label="Senha" type="password" value={form.password} onChange={(value) => onChange(field(form, 'password', value))} />
      )}
      <label className="input-field">
        <span>Tipo</span>
        <select value={form.userTypeId} onChange={(event) => onChange(field(form, 'userTypeId', event.target.value))}>
          {types.map((type) => (
            <option key={type.id} value={type.id}>{type.name}</option>
          ))}
        </select>
      </label>
      <AddressFields address={form.address} onChange={(address) => onChange(field(form, 'address', address))} />
      <SubmitButton saving={saving} />
    </form>
  );
}
