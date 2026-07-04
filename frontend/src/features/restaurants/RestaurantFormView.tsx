import type { FormEvent } from 'react';
import { AddressFields } from '../../components/common/AddressFields';
import { SubmitButton } from '../../components/common/SubmitButton';
import { TextField } from '../../components/common/TextField';
import { days } from '../../constants/modules';
import type { RestaurantForm, User } from '../../types';
import { field } from '../../utils/forms';

type RestaurantFormViewProps = {
  form: RestaurantForm;
  onChange: (form: RestaurantForm) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  owners: User[];
  saving: boolean;
};

export function RestaurantFormView({ form, onChange, onSubmit, owners, saving }: RestaurantFormViewProps) {
  return (
    <form className="entity-form" onSubmit={onSubmit}>
      <TextField label="Nome" value={form.name} onChange={(value) => onChange(field(form, 'name', value))} />
      <TextField label="CNPJ" value={form.cnpj} onChange={(value) => onChange(field(form, 'cnpj', value))} />
      <TextField label="Tipo de cozinha" value={form.type} onChange={(value) => onChange(field(form, 'type', value))} />
      <label className="input-field">
        <span>Dono</span>
        <select value={form.ownerId} onChange={(event) => onChange(field(form, 'ownerId', event.target.value))}>
          {owners.map((owner) => (
            <option key={owner.id} value={owner.id}>{owner.name}</option>
          ))}
        </select>
      </label>
      <AddressFields address={form.address} onChange={(address) => onChange(field(form, 'address', address))} />
      <fieldset className="fieldset">
        <legend>Funcionamento</legend>
        <div className="day-grid">
          {days.map((day) => (
            <label key={day}>
              <input
                checked={form.period.day.includes(day)}
                onChange={(event) => {
                  const nextDays = event.target.checked
                    ? [...form.period.day, day]
                    : form.period.day.filter((selectedDay) => selectedDay !== day);
                  onChange(field(form, 'period', { ...form.period, day: nextDays }));
                }}
                type="checkbox"
              />
              {day.slice(0, 3)}
            </label>
          ))}
        </div>
        <div className="two-columns">
          <TextField label="Abre" type="time" value={form.period.openTime} onChange={(value) => onChange(field(form, 'period', { ...form.period, openTime: value }))} />
          <TextField label="Fecha" type="time" value={form.period.closeTime} onChange={(value) => onChange(field(form, 'period', { ...form.period, closeTime: value }))} />
        </div>
      </fieldset>
      <SubmitButton saving={saving} />
    </form>
  );
}
