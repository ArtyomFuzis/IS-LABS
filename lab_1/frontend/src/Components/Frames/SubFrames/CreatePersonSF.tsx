import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsNotNull, processStringMaxLength, processStringMinLength } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate } from "../../../Utils/baseMaps"
import { createObject } from "../../../Utils/apiQueries"

function CreatePersonSF({ onClose }: { onClose: () => void}) {
    const [name, setName] = useState("")
    const [eyeColor, setEyeColor] = useState("")
    const [hairColor, setHairColor] = useState("")
    const [passportId, setPassportId] = useState("")
    const [nationality, setNationality] = useState("")
    const [location, setLocation] = useState(-1)
    
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    useEffect(() => {
        let res : string[] = []
        processStringMaxLength(name, 400, "Имя", res)
        processIsNotNull(name, "Имя", res)
        processStringMinLength(passportId, 6, "Паспорт", res)
        processStringMaxLength(passportId, 50, "Паспорт", res)
        setErrorMsg(res)
    }, [name, eyeColor, hairColor, passportId, nationality]);

    function createQuery(){
        let form = new URLSearchParams()
        form.append('name', name)
        form.append('eye_color_id', eyeColor)
        if(hairColor != "") form.append('hair_color_id', hairColor)
        if(location != -1) form.append('location_id', location.toString())
        if(passportId != "") form.append('passport_id', passportId)
        if(nationality != "") form.append('nationality_id', nationality)
        createObject(makeQueryCreate("Person"),form)
        onClose()
    }

    return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Создание Person</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={eyeColor} setValue={setEyeColor} required={true} type="enumSelectcolor" field="Цвет глаз" />
                    <ParameterField value={hairColor} setValue={setHairColor} required={false} type="enumSelectcolor" field="Цвет волос" />
                    <ParameterField value={passportId} setValue={setPassportId} required={false} type="text" field="Паспорт" />
                    <ParameterField value={nationality} setValue={setNationality} required={false} type="enumSelectcountry" field="Национальность" />
                    <ParameterField value={location} setValue={setLocation} required={false} type="chooseLocation" field="Адрес" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    Создать
                </button>
            </div>
        </>
    )
}

export default CreatePersonSF