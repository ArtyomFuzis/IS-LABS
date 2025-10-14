export const validateFloat = (input: string) => {
    const floatRegex = /^-?\d*[.,]?\d*$/;
    
    if (!floatRegex.test(input)) {
      return false;
    }

    return true;
};

export const validateInt = (input: string) => {
    const intRegex = /^-?\d*$/;
    
    if (!intRegex.test(input)) {
      return false;
    }

    return true;
};

export const processIsValidFloat = (flt: string, nullable: boolean, name: string, res: string[]) => {
    const badOutput = "Поле \"" + name + "\" должно быть валидным числом" + (nullable ? " или оставаться пустым" : "")
    const normalizedInput = flt?.replace(',', '.');
    if (isNaN(parseFloat(normalizedInput))) {
      if (flt?.trim() == "" && nullable) return;
      res.push(badOutput)
    }
}

export const processIsValidInt = (int: string, nullable: boolean, name: string, res: string[]) => {
    const badOutput = "Поле \"" + name + "\" должно быть валидным целым числом" + (nullable ? " или оставаться пустым" : "")
    if (isNaN(parseInt(int))) {
      if (int?.trim() == "" && nullable) return;
      res.push(badOutput)
    }
}

export const processIsNotNull = (text: string, name: string, res: string[]) => {
    const badOutput = "Поле \"" + name + "\" не должно оставаться пустым"
    if (text?.trim() == "") res.push(badOutput)
    
}

export const processStringMaxLength = (text: string, max_length: number, name: string, res: string[]) => {
    const badOutput = "Значение поля \"" + name + "\" не должно быть длинее " + max_length + " символов"
    if (text.length > max_length) res.push(badOutput)
}

export const processIsLowerThan = (num: string, max_val: number, equals: boolean, name: string, res: string[]) => {
    const badOutput = "Значение поля \"" + name + "\" должно быть меньше " +(equals?"(или равно) ":"")+ max_val
    const normalizedInput = num?.replace(',', '.');
    let flt = parseFloat(normalizedInput)
    if (isNaN(flt)) {
      return
    }
    if(flt < max_val || (flt == max_val && equals)){
      return
    }
    res.push(badOutput)
}

export const processIsBiggerThan = (num: string, min_val: number, equals: boolean, name: string, res: string[]) => {
    const badOutput = "Значение поля \"" + name + "\" должно быть больше " +(equals?"(или равно) ":"")+ min_val
    const normalizedInput = num?.replace(',', '.');
    let flt = parseFloat(normalizedInput)
    if (isNaN(flt)) {
      return
    }
    if(flt > min_val || (flt == min_val && equals)){
      return
    }
    res.push(badOutput)
}

export const processIsPositive = (num: string, equals: boolean, name: string, res: string[]) => {
    const badOutput = "Значение поля \"" + name + "\" должно быть больше " +(equals?"(или равно) нулю":"нуля")
    let int = parseInt(num)
    if (isNaN(int)) {
      return
    }
    if(int > 0 || (int == 0 && equals)){
      return
    }
    res.push(badOutput)
}