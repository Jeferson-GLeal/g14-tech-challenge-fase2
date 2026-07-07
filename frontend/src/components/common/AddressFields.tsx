import type { Address } from '../../types';
import { field } from '../../utils/forms';
import { TextField } from './TextField';

type AddressFieldsProps = {
  address: Address;
  onChange: (address: Address) => void;
};

export function AddressFields({ address, onChange }: AddressFieldsProps) {
  return (
    <fieldset className="fieldset">
      <legend>Endereco</legend>
      <TextField label="Logradouro" value={address.street} onChange={(value) => onChange(field(address, 'street', value))} />
      <div className="two-columns">
        <TextField label="Numero" value={address.number} onChange={(value) => onChange(field(address, 'number', value))} />
        <TextField label="Complemento" value={address.complement ?? ''} onChange={(value) => onChange(field(address, 'complement', value))} />
      </div>
      <TextField label="Bairro" value={address.district} onChange={(value) => onChange(field(address, 'district', value))} />
      <div className="two-columns">
        <TextField label="Cidade" value={address.city} onChange={(value) => onChange(field(address, 'city', value))} />
        <TextField label="UF" maxLength={2} value={address.state} onChange={(value) => onChange(field(address, 'state', value.toUpperCase()))} />
      </div>
      <TextField label="CEP" value={address.zipCode} onChange={(value) => onChange(field(address, 'zipCode', value))} />
    </fieldset>
  );
}
