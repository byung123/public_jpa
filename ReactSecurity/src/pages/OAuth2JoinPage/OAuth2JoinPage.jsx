/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { oauth2MergeApi, oauth2signupApi } from "../../apis/oauth2Api";

const layout = css`
    display: flex;
    flex-direction: column;
    margin: 0px auto;
    width: 460px;
`;

const logo = css`
    font-size: 24px;
    margin-bottom: 40px;
`;

const selectMenuBox = css`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    width: 100%;
    
    & > input {
        display: none;
    }

    & > label {
        box-sizing: border-box;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-grow: 1;
        border: 1px solid #dbdbdb;
        height: 40px;
        cursor: pointer;
    }

    & > label:nth-of-type(1) {
        border-top-left-radius: 10px;
        border-bottom-left-radius: 10px;
    }

    & > label:nth-last-of-type(1) {
        border-top-right-radius: 10px;
        border-bottom-right-radius: 10px;
    }

    & > input:checked + label {
        background-color: #fafafa;
        box-shadow: 0px 0px 5px #00000033 inset;
    }
`;

const joinInfoBox = css`
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
    width: 100%;

    & input {
        box-sizing: border-box;
        border: none;
        outline: none;
        width: 100%;
        height: 50px;
        font-size: 16px;
    }

    & p {
        margin: 0px 0px 10px 10px;
        color: #ff2f2f;
        font-size: 12px;
    }

    & > div {
        box-sizing: border-box;
        width: 100%;
        border: 1px solid #dbdbdb;
        border-bottom: none;
        padding: 0px 20px;
    }

    & div:nth-of-type(1) {
        border-top-left-radius: 10px;
        border-top-right-radius: 10px;
    }

    & div:nth-last-of-type(1) {
        border-bottom: 1px solid #dbdbdb;
        border-bottom-left-radius: 10px;
        border-bottom-right-radius: 10px;
    }
`;

const joinButton = css`
    border: none;
    border-radius: 10px;
    width: 100%;
    height: 50px;
    background-color: #999999;
    color: #ffffff;
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
`;

function OAuth2JoinPage(props) {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    const [ selectMenu, setSelectMenu ] = useState("join");

    const [ inputUser, setInputUser ] = useState({
        username: "",
        password: "",
        checkPassword: "",
        name: "",
        email: ""
    });

    const [ fieldErrorMessages, setFieldErrorMessages ] = useState({
        username: <></>,
        password: <></>,
        checkPassword: <></>,
        name: <></>,
        email: <></>
    });

    const handleSelectMenuOnChange = (e) => {
        setInputUser({
            username: "",
            password: "",
            checkPassword: "",
            name: "",
            email: ""
        });
        setFieldErrorMessages({
            username: <></>,
            password: <></>,
            checkPassword: <></>,
            name: <></>,
            email: <></>
        });
        setSelectMenu(e.target.value);
    }

    const handleInputUserOnChange = (e) => {
        setInputUser(inputUser => ({
            ...inputUser,
            [e.target.name]: e.target.value
        }));
    }

    // oAuth2Name은 주소창에 있기 때문에 searchParams로 가져옴
    // 키 값의 oauth2Name은 일부러 소문자로 해놓음
    // json이 "oa" 문자열을 자동으로 소문자로 인식되서 일부러
    const handleMergeSubmitOnClick = async () => {
        const mergeUser = {
            username: inputUser.username,
            password: inputUser.password,
            oauth2Name: searchParams.get("oAuth2Name"),
            provider: searchParams.get("provider")
        }
        const mergeData = await oauth2MergeApi(mergeUser);
        if(!mergeData.isSuccess) {
            if(mergeData.errorStatus === "loginError") {
                alert(mergeData.error);
                return;
            }
            if(mergeData.errorStatus === "fieldError") {
                showFieldErrorMessage(mergeData.error);
                return;
            }
        }
        alert("게정 통합이 완료되었습니다.");
        navigate("/user/login");
    }

    const handleJoinSubmitOnClick = async () => {
        const mergeSignupData = {
            username: inputUser.username,
            password: inputUser.password,
            checkPassword: inputUser.checkPassword,
            name: inputUser.name,
            email: inputUser.email,
            oauth2Name: searchParams.get("oAuth2Name"),
            provider: searchParams.get("provider")
        }
        // 강사님은 이렇게 간단히..
        // const joinuser = {
        //     ...inputUser,
        //     oauth2Name: searchParams.get("oAuth2Name"),
        //     provider: searchParams.get("provider")
        // }
        const response = await oauth2signupApi(mergeSignupData);
        if(!response.isSuccess) {
            alert(response);
            return;
        }
        alert("통합 회원가입이 완료되었습니다");
        navigate("/user/login");
    }

    const showFieldErrorMessage = (fieldErrors) => {
        let EmptyFieldErrors = {
            username: <></>,
            password: <></>,
            checkPassword: <></>,
            name: <></>,
            email: <></>
        };
        
        for(let fieldError of fieldErrors) {
            EmptyFieldErrors = {
                ...EmptyFieldErrors,
                [fieldError.field]: <p>{fieldError.defaultMessage}</p>
            }
        }
        setFieldErrorMessages(EmptyFieldErrors);
    }

    return (
        <div css={layout}>
            <Link to={"/"}><h1 css={logo}>사이트 로고</h1></Link>
            <div css={selectMenuBox}>
                <input type="radio" id="merge" name="selectMenu" 
                    onChange={handleSelectMenuOnChange}
                    checked={selectMenu === "merge"} value="merge" />
                <label htmlFor="merge">계정통합</label>

                <input type="radio" id="join" name="selectMenu" 
                    onChange={handleSelectMenuOnChange}
                    checked={selectMenu === "join"} value="join" />
                <label htmlFor="join">회원가입</label>
            </div>
            {
                selectMenu === "merge"
                ?
                <>
                    <div css={joinInfoBox}>
                        <div>
                            <input type="text" name='username' onChange={handleInputUserOnChange} value={inputUser.username} placeholder='아이디' />
                            {fieldErrorMessages.username}
                        </div>
                        <div>
                            <input type="password" name='password' onChange={handleInputUserOnChange} value={inputUser.password} placeholder='비밀번호'/>
                            {fieldErrorMessages.password}
                        </div>
                    </div>
                    <button css={joinButton} onClick={handleMergeSubmitOnClick}>통합하기</button>
                </>
                :
                <>
                    <div css={joinInfoBox}>
                        <div>
                            <input type="text" name='username' onChange={handleInputUserOnChange} value={inputUser.username} placeholder='아이디' />
                            {fieldErrorMessages.username}
                        </div>
                        <div>
                            <input type="password" name='password' onChange={handleInputUserOnChange} value={inputUser.password} placeholder='비밀번호'/>
                            {fieldErrorMessages.password}
                        </div>
                        <div>
                            <input type="password" name='checkPassword' onChange={handleInputUserOnChange} value={inputUser.checkPassword} placeholder='비밀번호 확인'/>
                            {fieldErrorMessages.checkPassword}
                        </div>
                        <div>
                            <input type="text" name='name' onChange={handleInputUserOnChange} value={inputUser.name} placeholder='성명'/>
                            {fieldErrorMessages.name}
                        </div>
                        <div>
                            <input type="email" name='email' onChange={handleInputUserOnChange} value={inputUser.email} placeholder='이메일주소'/>
                            {fieldErrorMessages.email}
                        </div>
                    </div>
                    <button css={joinButton} onClick={handleJoinSubmitOnClick}>가입하기</button>
                </>
            }
        </div>
    );
}

export default OAuth2JoinPage;