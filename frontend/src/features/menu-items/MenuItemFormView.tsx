import type { FormEvent } from 'react';
import { SubmitButton } from '../../components/common/SubmitButton';
import { TextField } from '../../components/common/TextField';
import type { MenuItemForm, Restaurant } from '../../types';
import { field } from '../../utils/forms';

type MenuItemFormViewProps = {
  disabled: boolean;
  form: MenuItemForm;
  onChange: (form: MenuItemForm) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
  selectedRestaurant?: Restaurant;
};

export function MenuItemFormView({
  disabled,
  form,
  onChange,
  onSubmit,
  saving,
  selectedRestaurant,
}: MenuItemFormViewProps) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <p className="context-line">{selectedRestaurant ? `Restaurante: ${selectedRestaurant.name}` : 'Selecione um restaurante.'}</p>
      <TextField disabled={disabled} label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <label className="input-field">
        <span>Descricao</span>
        <textarea disabled={disabled} value={form.description} onChange={(event) => onChange(field(form, 'description', event.target.value))} />
      </label>
      <TextField disabled={disabled} label="Preco" min="0" step="0.01" type="number" value={String(form.price)} onChange={(value) => onChange(field(form, 'price', Number(value)))} />
      <TextField disabled={disabled} label="Foto" value={form.photoPath} onChange={(value) => onChange(field(form, 'photoPath', value))} />
      <label className="checkbox-field">
        <input
          checked={form.availableOnlyAtRestaurant}
          disabled={disabled}
          onChange={(event) => onChange(field(form, 'availableOnlyAtRestaurant', event.target.checked))}
          type="checkbox"
        />
        Disponivel apenas no restaurante
      </label>
      <SubmitButton disabled={disabled} saving={saving} />
    </form>
  );
}
