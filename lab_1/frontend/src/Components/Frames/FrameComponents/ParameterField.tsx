import { validateFloat, validateInt } from "../../../Utils/validations";


function ParameterField({required, type, field, value, setValue}: 
    {required: boolean, type:string, field: string, value:any, setValue: React.Dispatch<React.SetStateAction<any>>}){
    const handleChangeFloat = (inputValue : string) => {        
        if (validateFloat(inputValue)) {
            setValue(inputValue);
        }
    };
    const handleChangeInt = (inputValue : string) => {        
        if (validateInt(inputValue)) {
            setValue(inputValue);
        }
    };
    return(
        <div className="modal-parameter">
            <div className="modal-parameter-label">{field}
                {required && <span className="modal-parameter-obligatory-mark">*</span>}</div>
            {type == "text" && <input type="text" className="modal-parameter-input" value={value} onChange={(e)=>{setValue(e.target.value)}}></input>}
            {type == "floatNum" && <input type="text" className="modal-parameter-input" value={value} onChange={(e)=>{handleChangeFloat(e.target.value)}}></input>}
            {type == "intNum" && <input type="text" className="modal-parameter-input" value={value} onChange={(e)=>{handleChangeInt(e.target.value)}}></input>}
        </div>
    )
}

export default ParameterField