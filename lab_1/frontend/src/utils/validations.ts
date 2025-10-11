export const validateFloat = (input: string) => {
    const floatRegex = /^-?\d*[.,]?\d*$/;
    
    if (!floatRegex.test(input)) {
      return false;
    }

    return true;
};

export const processIsValidFloat = (flt: any, nullable: boolean, name: string, res: string[]) => {
    const badOutput = "Поле \"" + name + "\" должно быть валидным числом" + (nullable ? " или оставаться пустым" : "")
    const normalizedInput = flt?.replace(',', '.');
    if (isNaN(parseFloat(normalizedInput))) {
      if (flt?.trim() == "" && nullable) return;
      res.push(badOutput)
    }
}

export const processIsNotNull = (text: any, name: string, res: string[]) => {
    const badOutput = "Поле \"" + name + "\" не должно оставаться пустым"
    if (text?.trim() == "") res.push(badOutput)
    
}