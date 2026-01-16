import { useEffect, useState } from "react";
import type { SelectDTO } from "../../../interface/transfer-data/SelectDTO";
import type { EnumVal } from "../../../interface/entity/EnumVal";
import { fetchData } from "../../../util/apiQueries";
import { makeQueryEnumValGet } from "../../../util/baseMaps";
import { validateFloat, validateInt } from "../../../util/validations";
import BaseFrame from "../BaseFrame";
import ChooseSF from "../subframe/ChooseSF";
import "../../../style/Frames.css"


function ParameterField({required, type, field, value, setValue}: 
    {required: boolean, type:string, field: string, value:any, setValue: React.Dispatch<React.SetStateAction<any>>}){
    const [enumVal, setEnumVal] = useState([{id: -1, val: "loading..."}])
    const [subModalOpen, setSubModalOpen] = useState("")
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
    useEffect(() => {
        if(type.startsWith("enumSelect")){
            (fetchData(makeQueryEnumValGet(type.replace('enumSelect','')))).then(data => {
                let res = (data as SelectDTO<EnumVal>).result
                if (!required){
                    res.unshift({id:-1, val:""})
                }
                setEnumVal(res) 
                if(required){
                    setValue(1)
                }
            })
            .catch(error => {
                console.error("Ошибка:", error);
            })  
        }
    }, [])
    function subModalOnClose(){
        setSubModalOpen("")
    }
    return(
        <>
        <BaseFrame isOpen={subModalOpen != ""} onClose={subModalOnClose} zindex={2000} width="60%" height="">
            <>
                {subModalOpen=="LocationChoose" && <ChooseSF onClose={subModalOnClose} objectName='Location' chosenRow={value} setChosenRow={setValue} required={required}/>}
                {subModalOpen=="CoordinateChoose" && <ChooseSF onClose={subModalOnClose} objectName='Coordinate' chosenRow={value} setChosenRow={setValue} required={required}/>}
                {subModalOpen=="DisciplineChoose" && <ChooseSF onClose={subModalOnClose} objectName='Discipline' chosenRow={value} setChosenRow={setValue} required={required}/>}
                {subModalOpen=="LabWorkChoose" && <ChooseSF onClose={subModalOnClose} objectName='LabWork' chosenRow={value} setChosenRow={setValue} required={required}/>}
                {subModalOpen=="PersonChoose" && <ChooseSF onClose={subModalOnClose} objectName='Person' chosenRow={value} setChosenRow={setValue} required={required}/>}
            </>
        </BaseFrame>
        <div className="modal-parameter">
            <div className="modal-parameter-label">{field}
            {required && <span className="modal-parameter-obligatory-mark">*</span>}</div>
            {type == "text" && <input type="text" className="modal-parameter-input" value={value} onChange={(e)=>{setValue(e.target.value)}}></input>}
            {type == "floatNum" && <input type="text" className="modal-parameter-input" value={value} onChange={(e)=>{handleChangeFloat(e.target.value)}}></input>}
            {type == "intNum" && <input type="text" className="modal-parameter-input" value={value} onChange={(e)=>{handleChangeInt(e.target.value)}}></input>}
            {type == "dateTime" && <input type="datetime-local" className="modal-parameter-input" value={value} onChange={(e)=>{setValue(e.target.value)}}></input>}
            {type.startsWith("enumSelect") && <select className="modal-parameter-input" value={value} onChange={(e)=>{setValue(e.target.value)}}>
                {enumVal.map(item => (
                    <option key = {item.id} value={item.id}>{item.val}</option>
                ))}
            </select>}
            {type.startsWith("choose") && <div className="param-field-button-container modal-parameter-input"><button className="menu-button param-field-button" onClick={() =>{
                setSubModalOpen(type.replace('choose', '')+"Choose")
            }}>{
                (value == -1) ? ("Выберите " + type.replace('choose', '')) : ("Выбраный id: " + value)
            }</button></div>}

        </div>
        </>
        
    )
}

export default ParameterField