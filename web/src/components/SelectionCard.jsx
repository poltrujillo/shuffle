import React from 'react';
import Image from 'next/image';

const SelectionCard = (props) => {
  return (
    <div className="bg-transparent rounded-2xl shadow-lg p-4 border border-purple-600 shadow-purple-700">
      <Image
        src={props.img}
        alt={props.alt}
        className="w-32 h-32 rounded-2xl object-cover"
        width={128}
        height={128}
      />
      <h3 className="text-lg font-bold mt-2 text-slate-100">{props.name}</h3>
    </div>
  );
};

export default SelectionCard;
